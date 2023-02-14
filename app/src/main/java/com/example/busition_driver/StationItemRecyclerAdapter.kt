package com.example.busition_driver

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.busition_driver.placeholder.PlaceholderContent.PlaceholderItem
import com.example.busition_driver.databinding.FragmentStationItemBinding


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
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int{
        Log.d("val size", values.size.toString())
        return values.size
    }

    inner class ViewHolder(binding: FragmentStationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.stationId
        val contentView: TextView = binding.stationTime

        override fun toString(): String {
            return super.toString() + " '" + "'"
        }
    }

}