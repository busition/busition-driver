package io.busition.driver.main_screen.recycler_view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import io.busition.driver.R
import io.busition.driver.appendString
import io.busition.driver.data.schedule_route.StationData
import io.busition.driver.databinding.FragmentStationItemBinding
import io.busition.driver.main_screen.MainScreen
import io.busition.driver.main_screen.PassengerListListener


class StationItemRecyclerAdapter(
    private val values: List<StationData>, private val activity: MainScreen? = null
) : RecyclerView.Adapter<StationItemRecyclerAdapter.ViewHolder>() {

    var passengerListListener : PassengerListListener ?= null
    var userInfoListListener : PassengerListListener ?= null

    fun setToggleListener(listener: PassengerListListener) {
        this.passengerListListener = listener
    }

    fun setUserInfoListener(listener: PassengerListListener) {
        this.userInfoListListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentStationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),

            )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        Log.d("position", position.toString())
        holder.stationName.text = item.station_name
        holder.stationPssengerNumber.text =
            appendString(item.nowPassengerNumber.toString(), " / ", item.passenger_count.toString())
        //그리는 부분 수정이 필요함
        if (item.station_state == "not arrived" || item.station_state == "done") {
            holder.startDot.visibility = View.INVISIBLE
            holder.endDot.visibility = View.INVISIBLE
            holder.busMidTop.visibility = View.INVISIBLE
            holder.busMidBottom.visibility = View.INVISIBLE
            holder.busTop.visibility = View.INVISIBLE
            holder.busBottom.visibility = View.INVISIBLE
            holder.stationDotTop.visibility = View.VISIBLE
            holder.stationDotBottom.visibility = View.VISIBLE
        } else if (item.station_state == "arrived") {
            // 버스 도착 시
            holder.startDot.visibility = View.INVISIBLE
            holder.endDot.visibility = View.INVISIBLE
            holder.busMidTop.visibility = View.VISIBLE
            holder.busMidBottom.visibility = View.VISIBLE
            holder.busTop.visibility = View.INVISIBLE
            holder.busBottom.visibility = View.INVISIBLE
            holder.stationDotTop.visibility = View.INVISIBLE
            holder.stationDotBottom.visibility = View.INVISIBLE
        } else if (item.station_state == "pass") {
            holder.startDot.visibility = View.INVISIBLE
            holder.endDot.visibility = View.INVISIBLE
            holder.busMidTop.visibility = View.INVISIBLE
            holder.busMidBottom.visibility = View.INVISIBLE
            holder.busTop.visibility = View.INVISIBLE
            holder.busBottom.visibility = View.VISIBLE
            holder.stationDotTop.visibility = View.VISIBLE
            holder.stationDotBottom.visibility = View.VISIBLE
        } else if (item.station_state == "next") {
            holder.startDot.visibility = View.INVISIBLE
            holder.endDot.visibility = View.INVISIBLE
            holder.busMidTop.visibility = View.INVISIBLE
            holder.busMidBottom.visibility = View.INVISIBLE
            holder.busTop.visibility = View.VISIBLE
            holder.busBottom.visibility = View.INVISIBLE
            holder.stationDotTop.visibility = View.VISIBLE
            holder.stationDotBottom.visibility = View.VISIBLE
        } else if (item.station_state == "start") {
            holder.startDot.visibility = View.VISIBLE
            holder.endDot.visibility = View.INVISIBLE
            holder.busMidTop.visibility = View.INVISIBLE
            holder.busMidBottom.visibility = View.INVISIBLE
            holder.busTop.visibility = View.INVISIBLE
            holder.busBottom.visibility = View.INVISIBLE
            holder.stationDotTop.visibility = View.VISIBLE
            holder.stationDotBottom.visibility = View.VISIBLE
        } else if (item.station_state == "end") {
            if(item.nowStation){
                holder.busTop.visibility = View.VISIBLE
                holder.busBottom.visibility = View.VISIBLE
            }else{
                holder.busTop.visibility = View.INVISIBLE
                holder.busBottom.visibility = View.INVISIBLE
            }
            holder.startDot.visibility = View.INVISIBLE
            holder.endDot.visibility = View.VISIBLE
            holder.busMidTop.visibility = View.INVISIBLE
            holder.busMidBottom.visibility = View.INVISIBLE
            holder.stationDotTop.visibility = View.INVISIBLE
            holder.stationDotBottom.visibility = View.INVISIBLE
            holder.line.visibility = View.INVISIBLE
        }



        holder.btn.setOnClickListener {
            Log.d("btn", "test")
//            val bundle = bundleOf("station_uuid" to item.station_uuid)
//            val navController = Navigation.findNavController(holder.itemView)
//            navController.navigate(
//                R.id.action_fragment_item_detial_screen_to_userInfoDataFragment,
//                bundle
//            )
            passengerListListener?.onToggleVisibility(true,item.station_uuid)


        }


    }


    override fun getItemCount(): Int {
        return values.size
    }


    inner class ViewHolder(binding: FragmentStationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val stationName: TextView = binding.stationName
        val stationPssengerNumber: TextView = binding.stationPassengerNum

        //        val station_delay: TextView = binding.stationTimeDelay
//        val station_time: TextView = binding.stationTime
//        val station_lay: LinearLayout = binding.stationTiemLay
        val btn: ImageButton = binding.button
        val stationCardView: LinearLayout = binding.stationCard
        val startDot: ImageView = binding.startDot
        val endDot: ImageView = binding.endDot
        val busMidTop: ImageView = binding.midBusDotTop
        val busMidBottom: ImageView = binding.midBusDotBottom
        val busTop: ImageView = binding.busDotTop
        val busBottom: ImageView = binding.busDotBottom
        val stationDotTop: ImageView = binding.stationDotTop
        val stationDotBottom: ImageView = binding.stationDotBottom
        val line: View = binding.bottomLine


        override fun toString(): String {
            return super.toString() + " '" + "'"
        }
    }

    companion object {
        var delay = 0

    }
}