package com.example.busition_driver

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import com.example.busition_driver.data.passenger_list.PassengerData


import com.example.busition_driver.databinding.FragmentUserInfoDataBinding
import com.example.busition_driver.main_screen.MainScreen

class MyuserInfoDataRecyclerViewAdapter(
    private val values: List<PassengerData>
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
        holder.itemNumber.text = (position+1).toString()
        holder.passengerName.text = item.passenger_name
        holder.contentView.text = "탑승 대기"
        holder.callBtn.setOnClickListener {
            val call = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.passenger_phone))
            ContextCompat.startActivity(holder.itemView.context, call, null)

        }
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
        val passengerName: TextView = binding.passengerName
        val contentView: TextView = binding.content
        val itemNumber: TextView = binding.itemNumber
        val callBtn: Button = binding.callBtn

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}

private fun MainScreen.Companion.startActivity(call: Intent) {
    startActivity(call)
}
