package com.example.busition_driver.main_screen

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.busition_driver.R
import com.example.busition_driver.databinding.FragmentScheduleItemBinding
import com.example.busition_driver.databinding.FragmentScheduleListBinding
import com.example.busition_driver.main_screen.placeholder.ScheduleClass


class MyItemRecyclerViewAdapter(
    private val values: MutableList<ScheduleClass>, private val binding: FragmentScheduleListBinding?
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentScheduleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.typeView.text = item.getShuttleDirection()
        holder.destinationView.text = item.getDestination()
        holder.timeView.text = item.getDepartureTime()
        viewItemDetails(holder, item)

    }
    private fun viewItemDetails(holder:ViewHolder, item: ScheduleClass) {
        holder.itemView.setOnClickListener {
            Log.d("onBindViewHolder ", item.toString())
            val bundle = bundleOf("item" to item.getRouteId())
            val navController = Navigation.findNavController(holder.itemView)
            navController.navigate(R.id.action_scheduleList_to_fragment_item_detial_screen,bundle)

        }
    }
    override fun getItemCount(): Int = values.size
    inner class ViewHolder(binding: FragmentScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val typeView: TextView = binding.itemType
        val destinationView: TextView = binding.itemDestination
        val timeView: TextView = binding.itemTime

        override fun toString(): String {
            return super.toString() + " '" + destinationView.text + "'"
        }
    }


}