package com.busition.driver

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.busition.driver.data.RetrofitServiceInterface
import com.busition.driver.data.passenger_list.PassengerData
import com.busition.driver.data.passenger_list.PassengerListData
import com.busition.driver.databinding.FragmentUserInfoItemListBinding
import com.busition.driver.main_screen.FragmentItemDetailScreen
import com.busition.driver.main_screen.FragmentItemDetailScreen.Companion.shuttle_direction
import com.busition.driver.placeholder.UserInfoDataContent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A fragment representing a list of Items.
 */
class UserInfoDataFragment : Fragment() {

    private var columnCount = 1
    private var station_id:Int = 0
    private lateinit var binding: FragmentUserInfoItemListBinding
    private lateinit var userInfoRecyclerAdapter: MyuserInfoDataRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        station_id = arguments?.getInt("station_id") ?: 0

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info_item_list, container, false)
        binding = FragmentUserInfoItemListBinding.bind(view)
        userInfoRecyclerAdapter = MyuserInfoDataRecyclerViewAdapter(UserInfoDataContent.ITEMS)
        binding.userInfoList.layoutManager = LinearLayoutManager(context)
        binding.userInfoList.adapter = userInfoRecyclerAdapter
        requestApi()
        return view
    }

    fun requestApi(){
        binding.userInfoList.gone()
        binding.loadBarUserInfo.show()
        val retrofit = Retrofit.Builder()
            .baseUrl(base_api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitServiceInterface::class.java)
        service.getPassengerList(FragmentItemDetailScreen.route_id,station_id, shuttle_direction ).enqueue(object : retrofit2.Callback<PassengerListData> {
            override fun onResponse(
                call: retrofit2.Call<PassengerListData>,
                response: retrofit2.Response<PassengerListData>
            ) {
                if (response.isSuccessful) {
                    val route = response.body()
                    Log.d("route", route.toString())
                    // 리사이클러 업데이트 ㅍ추가가가가가가가
                    val tmpList = ArrayList<PassengerData>()
                    for(i in 0 until route?.passengerList?.size!!){
                        val tmp = PassengerData(
                            route.passengerList[i].passenger_id ?:"",
                            route.passengerList[i].passenger_name ?:"",
                            route.passengerList[i].passenger_phone ?:"",
                            route.passengerList[i].passenger_email ?:"",
                        )
                        tmpList.add(tmp)
                    }

                    UserInfoDataContent.makeItem(tmpList)
                    userInfoRecyclerAdapter.notifyDataSetChanged()
                }else{
                    Log.d("route", "onResponse: ${response}")
                }
                binding.userInfoList.show()
                binding.loadBarUserInfo.gone()
            }

            override fun onFailure(call: retrofit2.Call<PassengerListData>, t: Throwable) {
                Log.d("route", "onFailure: ${t.message}")
            }
        })

    }
}