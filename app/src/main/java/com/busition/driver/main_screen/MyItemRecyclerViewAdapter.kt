package com.busition.driver.main_screen

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.busition.driver.R
import com.busition.driver.databinding.FragmentScheduleItemBinding
import com.busition.driver.databinding.FragmentScheduleListBinding
import com.busition.driver.main_screen.placeholder.PlaceholderContent


class MyItemRecyclerViewAdapter(
    private val values: MutableList<PlaceholderContent.PlaceholderItem>, private val binding: FragmentScheduleListBinding?
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
        val type:String
        if(item.shuttle_direction == 0){
            type = "등교"
        }else{
            type = "하교"
        }
        holder.departureView.text = "출발 : " + item.departure_point
        holder.typeView.text = type
        holder.destinationView.text = "종착 : " + item.destination
        holder.timeView.text = "다음 일정 : " + item.departure_time.day_time.time.S
//        holder.routeNameView.text = item.departure_time.route_name

        viewItemDetails(holder, item)

    }


    private fun viewItemDetails(holder:ViewHolder,item: PlaceholderContent.PlaceholderItem) {
        holder.itemView.setOnClickListener {
            Log.d("onBindViewHolder ", item.toString())

            val bundle = bundleOf("route_uuid" to item.route_uuid, "shuttle_direction" to item.shuttle_direction, "passListView" to true)
            val navController = Navigation.findNavController(holder.itemView)
            navController.navigate(R.id.action_scheduleList_to_fragment_item_detial_screen,bundle)

        }
    }
    override fun getItemCount(): Int  {
        Log.d("getItemCount", values.size.toString())
        Log.d("getItemCount", values.toString())
        return values.size
    }

    inner class ViewHolder(binding: FragmentScheduleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val typeView: TextView = binding.itemType
        val destinationView: TextView = binding.itemDestination
        val departureView: TextView = binding.itemDeparture
        val timeView: TextView = binding.itemTime
        val routeNameView:TextView = binding.routeName

        override fun toString(): String {
            return super.toString() + " '" + destinationView.text + "'"
        }
    }


}