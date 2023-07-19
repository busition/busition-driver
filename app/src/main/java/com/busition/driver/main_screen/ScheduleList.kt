package com.busition.driver.main_screen

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.busition.driver.R
import com.busition.driver.base_api_url
import com.busition.driver.data.schedule_all.DayTime
import com.busition.driver.data.schedule_all.DepartureTime
import com.busition.driver.data.RetrofitServiceInterface
import com.busition.driver.data.schedule_all.S
import com.busition.driver.data.schedule_all.ScheduleDataList

import com.busition.driver.databinding.FragmentScheduleListBinding
import com.busition.driver.gone
import com.busition.driver.main_screen.MainScreen.Companion.token
import com.busition.driver.main_screen.placeholder.PlaceholderContent
import com.busition.driver.show
import com.busition.driver.ui.login.LoginViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.GetTokenResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * A fragment representing a list of Items.
 */
class ScheduleList : Fragment() {

    private var columnCount = 1
    private var binding: FragmentScheduleListBinding? = null
    private val ANDROID_12_BLE_PERMISSIONS =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        arguments?.let {
//            userName = it.getString("userName").toString()
//            driver_id = it.getInt("driver_id")
//        }
        Log.d("scheduleList", "onCreate: $userName $driver_uuid")


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
        val contract = ActivityResultContracts.RequestMultiplePermissions()
        val activityResultLauncher = registerForActivityResult(contract) { permissions ->
            permissions.entries.forEach {
                Log.d("permission", "${it.key} = ${it.value}")
            }
            val isAllGranted = permissions.values.all { it == true }
            if (isAllGranted) {
                Log.d("permission", "all granted" + isAllGranted.toString())

//                Toast.makeText(activity, "permission granted", Toast.LENGTH_LONG).show()
            } else {
//                Toast.makeText(activity, "plz check permissions", Toast.LENGTH_LONG).show()
            }

        }
//        activityResultLauncher.launch(permissionList)

        activityResultLauncher.launch(ANDROID_12_BLE_PERMISSIONS)
        attachData()

        Log.d("scheduleList", "onCreateView: $userName $driver_uuid")

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
        Log.d("current user", LoginViewModel.auth_token)
        service.getSchedule(LoginViewModel.auth_token)
            .enqueue(object : retrofit2.Callback<ScheduleDataList> {
                override fun onResponse(
                    call: Call<ScheduleDataList>,
                    response: Response<ScheduleDataList>
                ) {
                    if (response.isSuccessful) {
                        val scheduleDataList = response.body()
                        Log.d("ScheduleDataList", scheduleDataList.toString())

                        if (scheduleDataList?.schedule_count != PlaceholderContent.ITEMS.size) {
                            PlaceholderContent.ITEMS.clear()
                            for (i in 0 until (scheduleDataList?.schedule_count ?: 0)) {
                                PlaceholderContent.addItem(
                                    PlaceholderContent.PlaceholderItem(

                                        scheduleDataList?.schedule_data?.get(i)?.route_uuid?: -1,
                                        scheduleDataList?.schedule_data?.get(i)?.departure_point
                                            ?: "",
                                        scheduleDataList?.schedule_data?.get(i)?.destination
                                            ?: "",
//                                                    scheduleDataList?.schedule_data?.get(i)?.departure_time
//                                                        ?: DepartureTime(
//                                                            DayTime(S(""), S(""))
//
//                                                        ),
                                        scheduleDataList?.schedule_data?.get(i)?.name ?: "",
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

                        Toast.makeText(
                            activity,
                            "Error for network",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    binding?.loadingBar?.gone()
                    binding?.recyclerScheduleList?.show()
                }

                override fun onFailure(call: Call<ScheduleDataList>, t: Throwable) {
                    Log.d("Error for network", t.toString())
                    Log.d("Error for network", MainScreen.token)
                    binding?.loadingBar?.gone()
                    Toast.makeText(
                        activity,
                        "Error for network",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }


    companion object {
        lateinit var recyclerViewAdapter: MyItemRecyclerViewAdapter
        var userName: String = "test"
        var driver_uuid: String = ""
        fun getUserData(userName: String, driver_uuid: String) {
            this.userName = userName
            this.driver_uuid = driver_uuid
        }

    }
}


