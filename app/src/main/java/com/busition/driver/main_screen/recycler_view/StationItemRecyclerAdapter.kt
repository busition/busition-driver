package com.busition.driver.main_screen.recycler_view

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
import com.busition.driver.R
import com.busition.driver.appendString
import com.busition.driver.data.schedule_route.StationData
import com.busition.driver.databinding.FragmentStationItemBinding
import com.busition.driver.main_screen.MainScreen
import com.busition.driver.main_screen.dataholder.StationDataContent


class StationItemRecyclerAdapter(
    private val values: List<StationData>, private val activity: MainScreen? = null
) : RecyclerView.Adapter<StationItemRecyclerAdapter.ViewHolder>() {

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
        holder.stationPssengerNumber.text = appendString(item.nowPassengerNumber.toString(), " / ", item.passenger_count.toString())
        if (item.station_state == "not arrived"){
            holder.startDot.visibility = View.INVISIBLE
            holder.endDot.visibility = View.INVISIBLE
            holder.busMidTop.visibility = View.INVISIBLE
            holder.busMidBottom.visibility = View.INVISIBLE
            holder.busTop.visibility = View.INVISIBLE
            holder.busBottom.visibility = View.INVISIBLE
            holder.stationDotTop.visibility = View.VISIBLE
            holder.stationDotBottom.visibility = View.VISIBLE
        }
        if(item.nowStation && position == 0){
            holder.startDot.visibility = View.VISIBLE
            holder.endDot.visibility = View.INVISIBLE
            holder.busMidTop.visibility = View.INVISIBLE
            holder.busMidBottom.visibility = View.INVISIBLE
            holder.busTop.visibility = View.INVISIBLE
            holder.busBottom.visibility = View.INVISIBLE
            holder.stationDotTop.visibility = View.VISIBLE
            holder.stationDotBottom.visibility = View.VISIBLE
        }
        if(position == values.size-1){
            holder.startDot.visibility = View.INVISIBLE
            holder.endDot.visibility = View.VISIBLE
            holder.busMidTop.visibility = View.INVISIBLE
            holder.busMidBottom.visibility = View.INVISIBLE
            holder.busTop.visibility = View.INVISIBLE
            holder.busBottom.visibility = View.INVISIBLE
            holder.stationDotTop.visibility = View.INVISIBLE
            holder.stationDotBottom.visibility = View.INVISIBLE
            holder.line.visibility = View.INVISIBLE
        }
        if(StationDataContent.ind-1 == position){
            holder.startDot.visibility = View.INVISIBLE
            holder.endDot.visibility = View.INVISIBLE
            holder.busMidTop.visibility = View.INVISIBLE
            holder.busMidBottom.visibility = View.INVISIBLE
            holder.busTop.visibility = View.INVISIBLE
            holder.busBottom.visibility = View.VISIBLE
            holder.stationDotTop.visibility = View.VISIBLE
            holder.stationDotBottom.visibility = View.VISIBLE
        }
        if(StationDataContent.ind == position){
            holder.startDot.visibility = View.INVISIBLE
            holder.endDot.visibility = View.INVISIBLE
            holder.busMidTop.visibility = View.INVISIBLE
            holder.busMidBottom.visibility = View.INVISIBLE
            holder.busTop.visibility = View.VISIBLE
            holder.busBottom.visibility = View.INVISIBLE
            holder.stationDotTop.visibility = View.VISIBLE
            holder.stationDotBottom.visibility = View.VISIBLE
        }



        holder.btn.setOnClickListener {
            Log.d("btn", "test")
            val bundle = bundleOf("station_uuid" to item.station_uuid)
            val navController = Navigation.findNavController(holder.itemView)
            navController.navigate(
                R.id.action_fragment_item_detial_screen_to_userInfoDataFragment,
                bundle
            )

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
        val busMidTop:ImageView = binding.midBusDotTop
        val busMidBottom:ImageView = binding.midBusDotBottom
        val busTop:ImageView = binding.busDotTop
        val busBottom:ImageView = binding.busDotBottom
        val stationDotTop:ImageView = binding.stationDotTop
        val stationDotBottom:ImageView = binding.stationDotBottom
        val line:View = binding.bottomLine


        override fun toString(): String {
            return super.toString() + " '" + "'"
        }
    }

    companion object{
        var delay = 0

    }
}