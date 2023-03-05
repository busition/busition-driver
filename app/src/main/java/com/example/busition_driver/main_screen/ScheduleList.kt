package com.example.busition_driver.main_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.busition_driver.R
import com.example.busition_driver.data.schedule_all.DayTime
import com.example.busition_driver.data.schedule_all.DepartureTime
import com.example.busition_driver.data.RetrofitServiceInterface
import com.example.busition_driver.data.schedule_all.ScheduleDataList

import com.example.busition_driver.databinding.FragmentScheduleListBinding
import com.example.busition_driver.main_screen.placeholder.PlaceholderContent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * A fragment representing a list of Items.
 */
class ScheduleList : Fragment() {

    private var columnCount = 1
    private var binding: FragmentScheduleListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        arguments?.let {
//            userName = it.getString("userName").toString()
//            driver_id = it.getInt("driver_id")
//        }
        Log.d("scheduleList", "onCreate: $userName $driver_id")


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_list, container, false)
        // Set the adapter


        if (view is RecyclerView) {
            binding = FragmentScheduleListBinding.inflate(inflater, container, false)
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


                service.getSchedule(PlaceholderContent.driver_id)
                    .enqueue(object : retrofit2.Callback<ScheduleDataList> {
                        override fun onResponse(
                            call: retrofit2.Call<ScheduleDataList>,
                            response: retrofit2.Response<ScheduleDataList>
                        ) {
                            if (response.isSuccessful) {
                                val scheduleDataList = response.body()
                                Log.d("ScheduleDataList", scheduleDataList.toString())

                                if (scheduleDataList?.schedule_count != PlaceholderContent.ITEMS.size) {
                                    for (i in 0 until (scheduleDataList?.schedule_count ?: 0)) {
                                        PlaceholderContent.addItem(
                                            PlaceholderContent.PlaceholderItem(
                                                scheduleDataList?.schedule_data?.get(i)?.route_id
                                                    ?: 0,
                                                scheduleDataList?.schedule_data?.get(i)?.departure_point
                                                    ?: "",
                                                scheduleDataList?.schedule_data?.get(i)?.destination
                                                    ?: "",
                                                scheduleDataList?.schedule_data?.get(i)?.departure_time
                                                    ?: DepartureTime(
                                                        DayTime("", "")
                                                    ),
                                                scheduleDataList?.schedule_data?.get(i)?.shuttle_direction
                                                    ?: 0,
                                            )
                                        )
                                    }
                                    Log.d("ITEMS", PlaceholderContent.ITEMS.toString())

                                }
                                adapter =
                                    MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS, binding)
                            } else {
                                Log.d("Error for network", response.toString())
                            }

                        }

                        override fun onFailure(
                            call: retrofit2.Call<ScheduleDataList>,
                            t: Throwable
                        ) {
                            Log.d("Error", t.toString())
                        }
                    })


            }

        }
        Log.d("scheduleList", "onCreateView: $userName $driver_id")

        return view
    }


    companion object {

        var userName: String = "test"
        var driver_id: Int = 0
        fun getUserData(userName: String, driver_id: Int) {
            this.userName = userName
            this.driver_id = driver_id
        }
    }
}


