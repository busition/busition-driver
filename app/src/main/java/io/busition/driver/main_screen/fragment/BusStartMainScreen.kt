package io.busition.driver.main_screen.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import io.busition.driver.*
import io.busition.driver.Service.Gps.GpsForegroundService
import io.busition.driver.data.RetrofitServiceInterface
import io.busition.driver.data.bus_route.RouteData
import io.busition.driver.data.schedule_route.StationData
import io.busition.driver.data.schedule_route.StationListData
import io.busition.driver.databinding.BusStartMainScreenBinding
import io.busition.driver.databinding.FragmentUserInfoItemListBinding
import io.busition.driver.main_screen.CallPassengerList
import io.busition.driver.main_screen.MainScreen
import io.busition.driver.main_screen.MainScreen.Companion.token
import io.busition.driver.main_screen.PassengerListListener
import io.busition.driver.main_screen.dataholder.StationDataContent
import io.busition.driver.main_screen.recycler_view.StationItemRecyclerAdapter
import io.busition.driver.roomDB.DataManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BusStartMainScreen : Fragment(), PassengerListListener {

    private lateinit var binding: BusStartMainScreenBinding


    private lateinit var station_count: TextView
    private lateinit var estimated_time: TextView
    private lateinit var passenger_count: TextView
    private lateinit var travel_distance: TextView
    private lateinit var route_name: String
    private lateinit var station_simple_info: LinearLayout
    private lateinit var station_list: LinearLayout
    private var passengerListCheck: Boolean = false
    private var route_list = ArrayList<ArrayList<Double>>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arg = requireArguments()
        route_uuid = arg.getInt("route_uuid")
        Log.d("route_uuid", route_uuid.toString())
        route_name = arg.getString("route_name").toString()

        /**
         * 뒤로가기 버튼을 눌렀을 때, 운행 종료 여부를 묻는다.
         */
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (passengerListCheck) {
                onToggleVisibility(false,0)
            } else if (busRunning) {
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle("운행 종료")
                        .setMessage("운행을 종료하시겠습니까?")
                        .setPositiveButton("네", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                val intent = Intent(getActivity(), MainScreen::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                                val gpsIntent = Intent(activity, GpsForegroundService::class.java)
                                gpsIntent.setAction("stop")
                                activity?.startService(gpsIntent)
                                busRunning = false
                                DataManager.clearDatabase()
                            }
                        })
                        .setNegativeButton("아니오", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {

                            }
                        })
                        .create()
                        .show()
                }
            } else {
                val intent = Intent(getActivity(), MainScreen::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bus_start_main_screen, container, false)
        binding = BusStartMainScreenBinding.bind(view)
        binding.itemList.stationListView.layoutManager = LinearLayoutManager(context)
        stationItemRecyclerAdapter =
            StationItemRecyclerAdapter(StationDataContent.ITEMS, activity as MainScreen)
        binding.itemList.stationListView.adapter = stationItemRecyclerAdapter
        binding.passenger.userInfoList.adapter = UserInfoDataFragment.userInfoRecyclerAdapter

        stationItemRecyclerAdapter.setToggleListener(this)
        binding.passenger.back.setOnClickListener {
            onToggleVisibility(false,0)
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * 구글 맵을 불러오기
         */
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        station_count = binding.numberOfStation
        estimated_time = binding.estimatedTime
        passenger_count = binding.numberOfPasenger
        travel_distance = binding.travelDistance
        station_simple_info = binding.staionSimpleInfo
        station_list = binding.stationList

        binding.routeName.text = route_name
        binding.routeName2.text = route_name
        binding.runBtn.isEnabled = false

//        open_passenger_list = false

        requestApiServer()
        if (!busRunning) {
            station_simple_info.show()
            station_list.gone()
        } else {
            station_list.show()
            station_simple_info.gone()
            Log.d("schedule list", stationDataList.toString())
        }

        binding.runBtn.setOnClickListener {
            MainScreen.test_code = false
            station_simple_info.gone()
            station_list.show()
            busRunning = true
//            scan()
            startForegroundService()
        }
        binding.stopBtn.setOnClickListener {
            val gpsIntent = Intent(activity, GpsForegroundService::class.java)
            gpsIntent.setAction("stop")
            activity?.startService(gpsIntent)
            val intent = Intent(getActivity(), MainScreen::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            busRunning = false
            DataManager.clearDatabase()
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(activity, io.busition.driver.Service.FloatingService::class.java)
            activity?.startService(intent)
            navigate()
        }

    }


    /**
     * 임시로 Tmap 연결하는 함수
     */
    fun navigate() {
        val uriTmap = Uri.parse("tmap://")
        var longitude = StationDataContent.ITEMS.get(StationDataContent.ind).station_longitude
        var latitude = StationDataContent.ITEMS.get(StationDataContent.ind).station_latitude
        val title = StationDataContent.ITEMS.get(StationDataContent.ind).station_name
        var intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("tmap://route?goalx=$longitude&goaly=$latitude&goalname=$title")
        ).apply {
            `package` = "com.skt.tmap.ku"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        startActivity(intent)
    }

    /**
     * ble 스캔 시작하는 함수
     */
    fun bleScan() {
        (activity as MainScreen?)?.scanLeDevice(true)
    }

    /**
     * 구글맵 콜백 함수
     */
    private val callback = OnMapReadyCallback { googleMap ->
        gmap = googleMap
        val seoul = LatLng(37.56, 126.97)
        val cameraPosition = CameraPosition.Builder()
            .target(seoul)
            .zoom(15f) // 줌 레벨 조정 가능
            .build()
        gmap!!.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    /**
     * GPS 관련 서비스 및 전반적인 백그라운드 서비스 시작
     */
    fun startForegroundService() {
        val gpsIntent = Intent(activity, GpsForegroundService::class.java)
        gpsIntent.setAction("start")
        activity?.startService(gpsIntent)
    }


    /**
     * 노선 경로 받아오기 위한 api 서버 연결 함수
     * 경로를 받아온 뒤에는 운행 시작 버튼 활성화
     */
    fun requestRouteApiServer() {
        route_list.clear()
        val retrofit = Retrofit.Builder()
            .baseUrl(base_api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitServiceInterface::class.java)
        service.getRoute(token, route_uuid)
            .enqueue(object : retrofit2.Callback<RouteData> {
                override fun onResponse(
                    call: retrofit2.Call<RouteData>,
                    response: retrofit2.Response<RouteData>
                ) {
                    if (response.isSuccessful) {
                        val route = response.body()
                        Log.d("api", route.toString())
                        Log.d(
                            "api",
                            "onResponse: ${response.body()}"
                        )
                        if (response.body()?.path?.get(0) != null) {
                            route_list =
                                route?.path as ArrayList<ArrayList<Double>>
                            Log.d("polypoly", route_list.toString())
                            makePolyRoute()
                        } else {
                            Log.d("polypoly", "null")
                            binding.runBtn.isEnabled = true
                        }

                    } else {

                        Log.d("api", "onResponse: $response")
                        binding.runBtn.isEnabled = true
                    }
                }

                override fun onFailure(call: retrofit2.Call<RouteData>, t: Throwable) {
                    Log.d("api", "onFailure: ${t.message}")
                }
            })
    }

    /**
     * 노선의 기본정보 받아오기 및 정거장의 세부정보 받아오기
     * -> 구글 지도에 정거장 좌표 찍기
     */
    fun requestApiServer() {
        binding.loadBar.show()
        Log.d("requestApiServer", "requestApiServer")
        StationDataContent.ITEMS.clear()
        stationDataList.clear()
//        binding.textList.gone()
        binding.loadBar.show()
        val retrofit = Retrofit.Builder()
            .baseUrl(base_api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitServiceInterface::class.java)
        service.getStation(token, route_uuid)
            .enqueue(object : retrofit2.Callback<StationListData> {
                override fun onResponse(
                    call: retrofit2.Call<StationListData>,
                    response: retrofit2.Response<StationListData>
                ) {
                    if (response.isSuccessful) {
                        val route = response.body()
                        Log.d("route", route.toString())
                        var tt: String = station_count.text as String
                        station_count.text =
                            appendString(
                                station_count.text.toString(),
                                route?.station_count.toString(),
                                " 개"
                            )
                        passenger_count.text =
                            appendString(
                                passenger_count.text.toString(),
                                route?.total_passenger_count.toString(),
                                " 명"
                            )
                        for (i in 0 until (route?.station_count ?: 0)) {
//                            DataManager.addStation(route?.station_data?.get(i)?.station_uuid.toString())
                            val tmp = StationData(
                                route?.station_data?.get(i)?.station_uuid ?: -1,
                                route?.station_data?.get(i)?.station_name ?: "",
                                route?.station_data?.get(i)?.stopping_order ?: 0,
                                route?.station_data?.get(i)?.station_latitude ?: 0.0,
                                route?.station_data?.get(i)?.station_longitude ?: 0.0,
                                route?.station_data?.get(i)?.passenger_count ?: 0,
                                route?.station_data?.get(i)?.travel_distance ?: "",
                            )
                            if (i == 0) {
                                tmp.nowStation = true
                                tmp.station_state = "start"
                            } else if (i == route?.station_count!! - 1) {
                                tmp.station_state = "end"
                            }
                            stationDataList.add(tmp)
                        }
                        Log.d("check list", stationDataList.toString())
                        Log.d(
                            "check size",
                            ScheduleList.scheduleRecyclerViewAdapter.itemCount.toString()
                        )
                        StationDataContent.makeItem(stationDataList)
                        ScheduleList.scheduleRecyclerViewAdapter.notifyDataSetChanged()


                    } else {
                        gmap?.clear()
                        Log.d("response", "onResponse: $response, $token")
                    }
                    binding.loadBar.gone()
                    station_list.show()
//                                binding.textList.show()
                    makeRoute()

                }

                override fun onFailure(
                    call: retrofit2.Call<StationListData>,
                    t: Throwable
                ) {
                    Log.d("route", "onFailure: ${t.message}")
                }
            })

    }

    /**
     * 구글맵에 정거장 좌표 찍기
     * -> 노선 경로 불러오는 api 호출
     */
    fun makeRoute() {
        gmap?.clear()
        var list: MutableList<LatLng> = mutableListOf()
        var tmp: LatLng = LatLng(0.0, 0.0)
        var boundsBuilder = LatLngBounds.Builder()
        if (StationDataContent.ITEMS.size != 0) {
            StationDataContent.ITEMS.forEach {
                tmp = LatLng(it.station_latitude, it.station_longitude)
                list.add(tmp)
                Log.d("makeRoute func", it.station_name)
                gmap?.addMarker(
                    MarkerOptions()
                        .position(tmp)
                        .title(it.station_name)
                )
                boundsBuilder.include(tmp)
            }

            var bounds = boundsBuilder.build()
            gmap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }
        requestRouteApiServer()
    }

    fun makePolyRoute() {
        var list: MutableList<LatLng> = mutableListOf()
        var tmp: LatLng = LatLng(0.0, 0.0)
        list.clear()
        for (i in 0 until route_list.size) {
            tmp = LatLng(route_list[i][1], route_list[i][0])
            list.add(tmp)


        }
        gmap?.addPolyline(
            PolylineOptions()
                .addAll(list)
                .width(10f)
                .color(Color.RED)
        )
        polyList = list
        binding.runBtn.isEnabled = true
    }

    fun onBackPressed() {
        Toast.makeText(context, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
    }

    fun tm() {
        station_simple_info.gone()
        station_list.gone()
    }

    /**
     * 소켓 응답을 받는 브로드캐스트 리시버
     */
    inner class socketBR : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1?.action == "update") {
                StationItemRecyclerAdapter.delay = p1.getIntExtra("delay", 0)
                stationItemRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        var callPassengerListListener : CallPassengerList?= null
        fun setListener(listener: CallPassengerList) {
            callPassengerListListener = listener
        }

        val stationDataList: MutableList<StationData> = ArrayList()
        var route_uuid: Int = -1
        var gmap: GoogleMap? = null
        var polyList: MutableList<LatLng> = LatLng(37.611983, 126.9976509).let {
            mutableListOf(
                it
            )
        }

        lateinit var stationItemRecyclerAdapter: StationItemRecyclerAdapter
        var check_passenger_list: MutableList<String> = ArrayList()
        var passenger_list: MutableList<String> = ArrayList()
        var busRunning: Boolean = false
        var t: Boolean = false
    }

    override fun onToggleVisibility(isVisible: Boolean, stationUuid: Int) {
        if (isVisible) {
            binding.passengerList.show()
            passengerListCheck = true
            station_simple_info.gone()
            station_list.gone()
            binding.passenger.loadBarUserInfo.show()
            callPassengerListListener?.callPassengerList(stationUuid)
            binding.passenger.loadBarUserInfo.gone()
            Log.d("passenger list", callPassengerListListener.toString())

        } else {
            binding.passengerList.gone()
            passengerListCheck = false
            if (!busRunning) {
                station_simple_info.show()
                station_list.gone()
            } else {
                station_list.show()
                station_simple_info.gone()
                Log.d("schedule list", stationDataList.toString())
            }

        }
    }
}