package com.busition.driver.main_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.busition.driver.R
import com.busition.driver.base_api_url
import com.busition.driver.data.schedule_all.DayTime
import com.busition.driver.data.schedule_all.DepartureTime
import com.busition.driver.data.RetrofitServiceInterface
import com.busition.driver.data.schedule_all.ScheduleDataList

import com.busition.driver.databinding.FragmentScheduleListBinding
import com.busition.driver.gone
import com.busition.driver.main_screen.MainScreen.Companion.token
import com.busition.driver.main_screen.placeholder.PlaceholderContent
import com.busition.driver.show
import com.busition.driver.ui.login.LoginViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.GetTokenResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * A fragment representing a list of Items.
 */
class ScheduleList : Fragment() {

    private var columnCount = 1
    private var binding: FragmentScheduleListBinding? = null
    private lateinit var recyclerViewAdapter: MyItemRecyclerViewAdapter

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

        binding = FragmentScheduleListBinding.bind(view)
        recyclerViewAdapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS, binding)
        binding!!.recyclerScheduleList.layoutManager = LinearLayoutManager(context)
        binding!!.recyclerScheduleList.adapter = recyclerViewAdapter

        attachData()

        Log.d("scheduleList", "onCreateView: $userName $driver_id")

        return view
    }

    fun attachData() {
        binding?.loadingBar?.show()
        binding?.recyclerScheduleList?.gone()
        val retrofit = Retrofit.Builder()
            .baseUrl(base_api_url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitServiceInterface::class.java)
        Log.d("current user", LoginViewModel.auth.currentUser.toString())
        LoginViewModel.auth.currentUser?.getIdToken(true)
            ?.addOnCompleteListener(OnCompleteListener<GetTokenResult> { t ->
                if (t.isSuccessful) {
                    val idToken = t.result.token
                    token = "Bearer "+idToken.toString()
                    Log.d("token", token)
                    service.getSchedule(token)
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
                                        recyclerViewAdapter?.notifyDataSetChanged()


                                    }

                                } else {
                                    Log.d("Error for network", response.toString())
                                    Log.d("Error for network", MainScreen.token)
                                }
                                binding?.recyclerScheduleList?.show()
                                binding?.loadingBar?.gone()
                            }

                            override fun onFailure(
                                call: retrofit2.Call<ScheduleDataList>,
                                t: Throwable
                            ) {
                                Log.d("Error", t.toString())
                            }
                        })


                } else {
                    // Handle error -> task.getException();
                }
            })

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


