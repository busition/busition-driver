package io.busition.driver.main_screen.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.busition.driver.R
import io.busition.driver.base_api_url
import io.busition.driver.data.RetrofitServiceInterface
import io.busition.driver.data.passenger_list.PassengerData
import io.busition.driver.data.passenger_list.PassengerListData
import io.busition.driver.databinding.FragmentUserInfoItemListBinding
import io.busition.driver.gone
import io.busition.driver.main_screen.MainScreen
import io.busition.driver.main_screen.MainScreen.Companion.token
import io.busition.driver.main_screen.recycler_view.MyuserInfoDataRecyclerViewAdapter
import io.busition.driver.main_screen.dataholder.UserInfoDataContent
import io.busition.driver.show
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * A fragment representing a list of Items.
 */
class UserInfoDataFragment : Fragment() {

    private var columnCount = 1
    private var station_uuid: String = ""
    private lateinit var binding: FragmentUserInfoItemListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        station_uuid = arguments?.getString("station_uuid") ?:""
        Log.d("station uuid ", station_uuid)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button2.setOnClickListener {
            requestApi()
        }
        binding.button3.setOnClickListener {
            scan()
        }
        binding.button4.setOnClickListener {
            for (i in 0 until MainScreen.user_uuid.size) {
                Log.d("user uuid", MainScreen.user_uuid[i])
            }
            MainScreen.user_uuid.clear()
            for (i in 0 until MainScreen.check_uuid.size){
                Log.d("check uuid", MainScreen.check_uuid[i])
            }
        }
        binding.button5.setOnClickListener{
            (activity as MainScreen?)?.scanLeDevice(false)
        }
    }
    fun scan(){
        (activity as MainScreen?)?.scanLeDevice(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_user_info_item_list, container, false)
        binding = FragmentUserInfoItemListBinding.bind(view)

        binding.userInfoList.layoutManager = LinearLayoutManager(context)
        binding.userInfoList.adapter = userInfoRecyclerAdapter
        requestApi()
        return view
    }

    fun requestApi() {
        if (MainScreen.user_type == "normal") {
            binding.userInfoList.gone()
            binding.loadBarUserInfo.show()
            val retrofit = Retrofit.Builder()
                .baseUrl(base_api_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service = retrofit.create(RetrofitServiceInterface::class.java)
            service.getPassengerList(
                token,
                BusStartMainScreen.route_uuid,
                station_uuid,
            ).enqueue(object : retrofit2.Callback<PassengerListData> {
                override fun onResponse(
                    call: retrofit2.Call<PassengerListData>,
                    response: retrofit2.Response<PassengerListData>
                ) {
                    if (response.isSuccessful) {
                        val route = response.body()
                        Log.d("route", route.toString())
                        // 리사이클러 업데이트 ㅍ추가가가가가가가
                        val tmpList = ArrayList<PassengerData>()
                        for (i in 0 until route?.passengerList?.size!!) {
                            val tmp = PassengerData(
                                route.passengerList[i].passenger_uid ?: "",
                                route.passengerList[i].passenger_name ?: "",
                                route.passengerList[i].passenger_phone ?: "",
                                route.passengerList[i].passenger_email ?: "",
                            )
                            tmpList.add(tmp)
                        }

                        UserInfoDataContent.makeItem(tmpList)
                        userInfoRecyclerAdapter.notifyDataSetChanged()
                    } else {
                        Log.d("fail ", "onResponse: ${response}")

                    }
                    binding.userInfoList.show()
                    binding.loadBarUserInfo.gone()
                }

                override fun onFailure(call: retrofit2.Call<PassengerListData>, t: Throwable) {
                    Log.d("fail", "onFailure: ${t.message}")
                    Log.d("fail ", "onResponse: ${call}")
                }
            })
        }
    }
    companion object{
        var userInfoRecyclerAdapter: MyuserInfoDataRecyclerViewAdapter = MyuserInfoDataRecyclerViewAdapter(
            UserInfoDataContent.ITEMS)
    }
}
