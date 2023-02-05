package com.example.busition_driver.main_screen

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.busition_driver.R
import com.example.busition_driver.databinding.FragmentScheduleItemBinding

import com.example.busition_driver.main_screen.placeholder.PlaceholderContent.PlaceholderItem


class MyItemRecyclerViewAdapter(
        private val values: List<PlaceholderItem>)
    : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(FragmentScheduleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.typeView.text = item.typeData
        holder.destinationView.text = item.destinationData
        holder.timeView.text = item.timeData
    }

    override fun getItemCount(): Int = values.size
    inner class ViewHolder(binding: FragmentScheduleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val typeView: TextView = binding.itemType
        val destinationView: TextView = binding.itemDestination
        val timeView: TextView = binding.itemTime

        override fun toString(): String {
            return super.toString() + " '" + destinationView.text + "'"
        }
    }

}