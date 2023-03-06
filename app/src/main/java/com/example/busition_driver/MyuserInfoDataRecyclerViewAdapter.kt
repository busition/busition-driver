package com.example.busition_driver

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.busition_driver.data.schedule_route.StationData


import com.example.busition_driver.databinding.FragmentUserInfoDataBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyuserInfoDataRecyclerViewAdapter(
    private val values: List<StationData>
) : RecyclerView.Adapter<MyuserInfoDataRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentUserInfoDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = "유경민"
        holder.contentView.text = "탑승 대기"
    }

    override fun getItemCount(): Int {
        if(values.size == 0){
            return  10
        }else{
            return values.size
        }
    }

    inner class ViewHolder(binding: FragmentUserInfoDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}