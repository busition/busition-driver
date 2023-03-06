package com.example.busition_driver

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.busition_driver.data.RetrofitServiceInterface
import com.example.busition_driver.data.passenger_list.PassengerListData
import com.example.busition_driver.data.schedule_route.RouteData
import com.example.busition_driver.data.schedule_route.StationData
import com.example.busition_driver.main_screen.FragmentItemDetailScreen
import com.example.busition_driver.placeholder.PlaceholderContent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A fragment representing a list of Items.
 */
class UserInfoDataFragment : Fragment() {

    private var columnCount = 1
    private var station_id:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        station_id = arguments?.getInt("station_id") ?: 0

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_info_item_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://api.busition.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val service = retrofit.create(RetrofitServiceInterface::class.java)
//                service.getPassengerList(FragmentItemDetailScreen.route_id,station_id).enqueue(object : retrofit2.Callback<PassengerListData> {
//                    override fun onResponse(
//                        call: retrofit2.Call<PassengerListData>,
//                        response: retrofit2.Response<PassengerListData>
//                    ) {
//                        if (response.isSuccessful) {
//                            val route = response.body()
//                            Log.d("route", route.toString())
//                            // 리사이클러 업데이트 ㅍ추가가가가가가가
//
//                        }else{
//                            Log.d("route", "onResponse: ${response}")
//                        }
//                    }
//
//                    override fun onFailure(call: retrofit2.Call<PassengerListData>, t: Throwable) {
//                        Log.d("route", "onFailure: ${t.message}")
//                    }
//                })
                adapter = MyuserInfoDataRecyclerViewAdapter(PlaceholderContent.ITEMS)
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            UserInfoDataFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}