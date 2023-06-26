package com.busition.driver.main_screen

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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.busition.driver.*
import com.busition.driver.data.RetrofitServiceInterface
import com.busition.driver.data.bus_route.RouteData
import com.busition.driver.data.schedule_route.StationData
import com.busition.driver.data.schedule_route.StationListData
import com.busition.driver.databinding.FragmentItemDetailScreenBinding
import com.busition.driver.main_screen.MainScreen.Companion.token
import com.busition.driver.placeholder.PlaceholderContent
import com.busition.driver.ui.login.LoginViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.GetTokenResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentItemDetailScreen : Fragment() {

    private lateinit var binding: FragmentItemDetailScreenBinding


    private lateinit var station_count: TextView
    private lateinit var estimated_time: TextView
    private lateinit var passenger_count: TextView
    private lateinit var travel_distance: TextView
    private  var passListView:Boolean = false

    private var route_list = ArrayList<ArrayList<Double>>()

    private var permissionRequestCode = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arg = requireArguments()
        route_uuid = arg.getString("route_uuid").toString()
        Log.d("route_uuid", route_uuid)
        shuttle_direction = arg.getInt("shuttle_direction")
        passListView = arg.getBoolean("passListView")
        Log.d("passs", passListView.toString())
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            if(test){
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
                                test = false
                            }
                        })
                        .setNegativeButton("아니오", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {

                            }
                        })
                        .create()
                        .show()
                }
            }else{
                val intent = Intent(getActivity(), MainScreen::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }


        }
        //imsi use key


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        station_count = binding.numberOfStation
        estimated_time = binding.estimatedTime
        passenger_count = binding.numberOfPasenger
        travel_distance = binding.travelDistance
        binding.runBtn.isEnabled = false
        requestApiServer()
        requestRouteApiServer()
        if (!test) {
            binding.textList.show()
            binding.itemListLay.gone()
        }else{
            binding.itemListLay.show()
            binding.textList.gone()
            Log.d("schedule list", stationDataList.toString())
        }

        binding.runBtn.setOnClickListener {
            MainScreen.test_code = false
            val bundle = bundleOf("item" to binding.estimatedTime.text)
            val navController = Navigation.findNavController(view)
//            navController.navigate(
//                R.id.action_fragment_item_detial_screen_to_fragment_station_item_screen,
//                bundle
//            )
            binding.textList.gone()
            binding.itemListLay.show()
            test = true
            //service start func
            scan()
            tmp()


        }
        binding.stopBtn.setOnClickListener{

            val gpsIntent = Intent(activity, GpsForegroundService::class.java)
            gpsIntent.setAction("stop")
            activity?.startService(gpsIntent)
//            Toast.makeText(activity, "service stop", Toast.LENGTH_LONG).show()
            val intent = Intent(getActivity(), MainScreen::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            test = false
        }

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(activity, FloatingService::class.java)

            activity?.startService(intent)
            navigate()
        }

    }


    fun navigate(){
//        val uriNaverMap = Uri.parse("navermaps://")
//        val uriKakaoNavi = Uri.parse("kakaonavi://")
        val uriTmap = Uri.parse("tmap://")

//        val intent = Intent(Intent.ACTION_VIEW)
//        intent.data = Uri.parse("geo:0,0?q="+PlaceholderContent.ITEMS.get(0).station_latitude+","+PlaceholderContent.ITEMS.get(0).station_longitude) // 출발지 좌표
//        intent.putExtra(Intent.EXTRA_TITLE, "Select navigation app") // 다이얼로그 타이틀
//        intent.putExtra(
//            Intent.EXTRA_INITIAL_INTENTS, arrayOf(
////                Intent(Intent.ACTION_VIEW, uriNaverMap),
////                Intent(Intent.ACTION_VIEW, uriKakaoNavi),
//                Intent(Intent.ACTION_VIEW, uriTmap)
//            )
//        )

        var longitude = PlaceholderContent.ITEMS.get(PlaceholderContent.ind).station_longitude
        var latitude = PlaceholderContent.ITEMS.get(PlaceholderContent.ind).station_latitude
        val title = PlaceholderContent.ITEMS.get(PlaceholderContent.ind).station_name
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse("tmap://route?goalx=$longitude&goaly=$latitude&goalname=$title")).apply {
            `package` = "com.skt.tmap.ku"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        startActivity(intent)
    }

    fun scan(){
        (activity as MainScreen?)?.scanLeDevice(true)
    }
    private val callback = OnMapReadyCallback { googleMap ->
        gmap = googleMap
    }

    fun tmp() {
        val gpsIntent = Intent(activity, GpsForegroundService::class.java)
        gpsIntent.setAction("start")
        activity?.startService(gpsIntent)
//        Toast.makeText(activity, "service start", Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_detail_screen, container, false)
        binding = FragmentItemDetailScreenBinding.bind(view)
        binding.itemList.stationListView.layoutManager = LinearLayoutManager(context)
        stationItemRecyclerAdapter =
            StationItemRecyclerAdapter(PlaceholderContent.ITEMS, activity as MainScreen)
        binding.itemList.stationListView.adapter = stationItemRecyclerAdapter


        return view
    }

    fun requestRouteApiServer() {
        PlaceholderContent.ITEMS.clear()
        route_list.clear()
        val retrofit = Retrofit.Builder()
            .baseUrl(base_api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitServiceInterface::class.java)
        LoginViewModel.auth.currentUser?.getIdToken(true)
            ?.addOnCompleteListener(OnCompleteListener<GetTokenResult> { t ->
                if (t.isSuccessful) {
                    val idToken = t.result.token
                    token = "Bearer " + idToken.toString()
                    Log.d("id token", idToken.toString())
                    Log.d("requestRouteApiServer", "token : $route_uuid")
                    service.getRoute(token, route_uuid, shuttle_direction)
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
                                    if (response.body()?.path?.get(0) != null){
                                        route_list =
                                            route?.path as ArrayList<ArrayList<Double>>
                                        Log.d("polypoly", route_list.toString())
                                        makePolyRoute()
                                    }else{
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

                } else {
                    // Handle error -> task.getException();
                }
            })

    }

    fun requestApiServer() {
        binding.loadBar.show()
        Log.d("requestApiServer", "requestApiServer")
        PlaceholderContent.ITEMS.clear()
        stationDataList.clear()
//        binding.textList.gone()
//        binding.loadBar.show()
        val retrofit = Retrofit.Builder()
            .baseUrl(base_api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitServiceInterface::class.java)
        LoginViewModel.auth.currentUser?.getIdToken(true)
            ?.addOnCompleteListener(OnCompleteListener<GetTokenResult> { t ->
                if (t.isSuccessful) {
                    val idToken = t.result.token
                    token = "Bearer " + idToken.toString()
                    Log.d("id token", idToken.toString())
                    service.getStation(token, route_uuid, shuttle_direction)
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
                                            route?.station_count.toString()
                                        )
                                    passenger_count.text =
                                        appendString(
                                            passenger_count.text.toString(),
                                            route?.total_passenger_count.toString(),
                                            " 명"
                                        )
                                    for (i in 0 until (route?.station_count ?: 0)) {
                                        val tmp = StationData(
                                            route?.station_data?.get(i)?.station_uuid ?: "",
                                            route?.station_data?.get(i)?.station_name ?: "",
                                            route?.station_data?.get(i)?.stopping_order ?: 0,
                                            route?.station_data?.get(i)?.station_latitude ?: 0.0,
                                            route?.station_data?.get(i)?.station_longitude ?: 0.0,
                                            route?.station_data?.get(i)?.passenger_count ?: 0
                                        )
                                        stationDataList.add(tmp)
                                    }
                                    Log.d("check list", stationDataList.toString())
                                    Log.d("check size", ScheduleList.recyclerViewAdapter.itemCount.toString())
                                    PlaceholderContent.makeItem(stationDataList)
                                    ScheduleList.recyclerViewAdapter.notifyDataSetChanged()


                                } else {
                                    gmap?.clear()
                                    Log.d("response", "onResponse: $response")
                                }
                                binding.loadBar.gone()
                                binding.itemListLay.show()
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

                } else {
                    // Handle error -> task.getException();
                }
            })


    }

    fun makeRoute() {
        gmap?.clear()
        var list: MutableList<LatLng> = mutableListOf()
        var tmp: LatLng = LatLng(0.0, 0.0)
        var boundsBuilder = LatLngBounds.Builder()
        if (PlaceholderContent.ITEMS.size != 0) {
            PlaceholderContent.ITEMS.forEach {
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


    //in use global service google route api decode code
//    fun getRouteUrl() {
//        var origin: String = ""
//        var dest: String = ""
//        var waypoints: String = "&waypoints="
//
//        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=Disneyland&destination=Universal+Studios+Hollywood"+
//                "&key=AIzaSyBUNnlsuMCJth1VWvY4HV0pa5eoFmqya_A"
//        Log.d("route", url)
//        GetDirection(url).execute()
//    }


//    @SuppressLint("StaticFieldLeak")
//    private inner class GetDirection(val url: String) :
//        AsyncTask<Void, Void, List<List<LatLng>>>() {
//        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
//            val client = OkHttpClient()
//            val request = Request.Builder().url(url).build()
//            val response = client.newCall(request).execute()
//            val data = response.body()?.string()
//            Log.d("route", data.toString())
//            val result = ArrayList<List<LatLng>>()
//            try {
//                val respObj = Gson().fromJson(data, MapData::class.java)
//                val path = ArrayList<LatLng>()
//                for (i in 0 until respObj.routes[0].legs[0].steps.size) {
//                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
//                }
//                result.add(path)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            return result
//        }
//
//        override fun onPostExecute(result: List<List<LatLng>>) {
//            val lineoption = PolylineOptions()
//            for (i in result.indices) {
//                lineoption.addAll(result[i])
//                lineoption.width(10f)
//                lineoption.color(Color.GREEN)
//                lineoption.geodesic(true)
//            }
//            gmap.addPolyline(lineoption)
//        }
//    }


//    fun decodePolyline(encoded: String): List<LatLng> {
//        val poly = ArrayList<LatLng>()
//        var index = 0
//        val len = encoded.length
//        var lat = 0
//        var lng = 0
//        while (index < len) {
//            var b: Int
//            var shift = 0
//            var result = 0
//            do {
//                b = encoded[index++].code - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lat += dlat
//            shift = 0
//            result = 0
//            do {
//                b = encoded[index++].code - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lng += dlng
//            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
//            poly.add(latLng)
//        }
//        Log.d("poly", poly.toString())
//        return poly
//
//    }

    fun onBackPressed() {
            Toast.makeText(context, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
    }
    fun tm(){
    binding.textList.gone()
    binding.itemListLay.gone()
    }
    inner class socketBR : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1?.action == "update") {
                StationItemRecyclerAdapter.delay = p1.getIntExtra("delay", 0)
                stationItemRecyclerAdapter.notifyDataSetChanged()
            }
        }
    }

    companion object {

        val stationDataList: MutableList<StationData> = ArrayList()
        var route_uuid: String = ""
        var shuttle_direction: Int = 0
        var gmap: GoogleMap? = null
        var polyList: MutableList<LatLng> = LatLng(37.611983, 126.9976509).let {
            mutableListOf(
                it
            )
        }
        lateinit var stationItemRecyclerAdapter: StationItemRecyclerAdapter
        var check_passenger_list: MutableList<String> = ArrayList()
        var passenger_list: MutableList<String> = ArrayList()
        var test:Boolean = false
        var t:Boolean = false
    }
}