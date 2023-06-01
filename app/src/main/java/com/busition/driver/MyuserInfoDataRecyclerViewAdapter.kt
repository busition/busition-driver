package com.busition.driver

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import com.busition.driver.data.passenger_list.PassengerData


import com.busition.driver.databinding.FragmentUserInfoDataBinding
import com.busition.driver.main_screen.MainScreen
import java.util.UUID

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
    fun generateUUID(string: String): String {
        return UUID.fromString(string).toString()
    }
    fun compressString(string: String): String {
        var tmp :String =""
        if (string.length > 16) {
            tmp = string.substring(0, 8) + string.substring(string.length - 8)
            tmp = tmp.encodeToByteArray().joinToString("") { it.toString(16) }
        } else {
            tmp = string.encodeToByteArray().joinToString("") { it.toString(16) }
        }
        tmp = generateUUID(tmp)
        return tmp
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.itemNumber.text = (position+1).toString()
        holder.passengerName.text = item.passenger_name
        holder.contentView.text = "탑승 대기"
        if (position == 0 && MainScreen.test_code){
            holder.contentView.text = "탑승중"
            holder.contentView.setBackgroundColor("#8DA6FF".toColorInt())
        }

        if (MainScreen.user_uuid.contains(compressString(item.passenger_uid))){
            holder.contentView.text = "탑승중"
            holder.contentView.setBackgroundColor("#8DA6FF".toColorInt())
        }else if (!MainScreen.check_uuid.contains(item.passenger_uid)){
            MainScreen.check_uuid.add(compressString(item.passenger_uid))
        }
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
