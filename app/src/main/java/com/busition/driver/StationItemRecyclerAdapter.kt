package com.busition.driver

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.busition.driver.data.schedule_route.StationData
import com.busition.driver.databinding.FragmentItemDetailScreenBinding
import com.busition.driver.databinding.FragmentStationItemBinding
import com.busition.driver.main_screen.FragmentItemDetailScreen
import com.busition.driver.main_screen.MainScreen
import com.busition.driver.placeholder.PlaceholderContent


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
//        holder.station_time.text = item.station_id.toString()
        if (item.nowStation && MainScreen.stop){
            holder.startDot.visibility = View.GONE
            holder.endDot.visibility = View.GONE

            holder.busMidTop.visibility = View.VISIBLE
            holder.busMidBottom.visibility = View.VISIBLE

            holder.busTop.visibility = View.GONE
            holder.busBottom.visibility = View.GONE

            holder.stationDotTop.visibility = View.VISIBLE
            holder.stationDotBottom.visibility = View.VISIBLE
        }else if (!MainScreen.stop){
            if(position == PlaceholderContent.ind){
                holder.startDot.visibility = View.GONE
                holder.endDot.visibility = View.GONE

                holder.busMidTop.visibility = View.GONE
                holder.busMidBottom.visibility = View.GONE

                holder.busTop.visibility = View.GONE
                holder.busBottom.visibility = View.VISIBLE

                holder.stationDotTop.visibility = View.VISIBLE
                holder.stationDotBottom.visibility = View.VISIBLE
            }else if(position == PlaceholderContent.ind+1) {
                holder.startDot.visibility = View.GONE
                holder.endDot.visibility = View.GONE

                holder.busMidTop.visibility = View.GONE
                holder.busMidBottom.visibility = View.GONE

                holder.busTop.visibility = View.VISIBLE
                holder.busBottom.visibility = View.GONE

                holder.stationDotTop.visibility = View.VISIBLE
                holder.stationDotBottom.visibility = View.VISIBLE
            }
        }else{
            holder.startDot.visibility = View.GONE
            holder.endDot.visibility = View.GONE

            holder.busMidTop.visibility = View.GONE
            holder.busMidBottom.visibility = View.GONE

            holder.busTop.visibility = View.GONE
            holder.busBottom.visibility = View.GONE

            holder.stationDotTop.visibility = View.VISIBLE
            holder.stationDotBottom.visibility = View.VISIBLE
        }
//        if (delay == 0){
//            holder.station_lay.gone()
//        }else{
//            holder.station_lay.show()
//            holder.station_delay.text = delay.toString()
//        }

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



        override fun toString(): String {
            return super.toString() + " '" + "'"
        }
    }

    companion object{
        var delay = 0

    }
}