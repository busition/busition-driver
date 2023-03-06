package com.example.busition_driver

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.busition_driver.data.schedule_route.StationData
import com.example.busition_driver.databinding.FragmentStationItemBinding
import com.example.busition_driver.main_screen.MainScreen



class StationItemRecyclerAdapter(
    private val values: List<StationData>, private val activity: MainScreen? = null
) : RecyclerView.Adapter<StationItemRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentStationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        Log.d("position", position.toString())
            holder.station_id.text = item.station_name
            holder.station_time.text = item.station_id.toString()
            holder.btn.setOnClickListener {
                Log.d("btn", "test")
                val bundle = bundleOf("station_id" to item.station_id)
                val navController = Navigation.findNavController(holder.itemView)
                navController.navigate(R.id.action_fragment_station_item_screen_to_userInfoDataFragment, bundle)
            }


    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(binding: FragmentStationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val station_id: TextView = binding.stationIdContents
        val station_time: TextView = binding.stationTimeContents
        val station_lay: LinearLayout = binding.stationTiemLay
        val btn: Button = binding.button


        override fun toString(): String {
            return super.toString() + " '" + "'"
        }
    }

}