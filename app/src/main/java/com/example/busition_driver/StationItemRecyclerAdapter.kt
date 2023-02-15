package com.example.busition_driver

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.busition_driver.databinding.FragmentStationItemBinding
import com.example.busition_driver.placeholder.PlaceholderContent.PlaceholderItem


class StationItemRecyclerAdapter(
    private val values: List<PlaceholderItem>
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
        if (item.type == "test") {
            holder.station_lay.gone()
            holder.station_time.text = "test"
            holder.station_id.text = "test"
            holder.btn.setOnClickListener {
                Log.d("btn", "test")

            }
        } else {
            holder.station_id.text = item.id
            holder.station_time.text = item.content
            holder.btn.setOnClickListener {
                Log.d("btn", "1")
            }
        }

    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(binding: FragmentStationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val station_id: TextView = binding.stationId
        val station_time: TextView = binding.stationTime
        val station_lay: LinearLayout = binding.stationTiemLay
        val btn: Button = binding.button
        val _binding = binding

        override fun toString(): String {
            return super.toString() + " '" + "'"
        }
    }

}