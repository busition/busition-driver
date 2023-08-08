package io.busition.driver.main_screen.recycler_view

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import io.busition.driver.data.passenger_list.PassengerData
import io.busition.driver.databinding.FragmentUserInfoDataBinding
import io.busition.driver.gone
import io.busition.driver.main_screen.CallPassengerList
import io.busition.driver.main_screen.MainScreen
import io.busition.driver.main_screen.PassengerListListener
import io.busition.driver.main_screen.dataholder.UserInfoDataContent
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
        val charSet = Charsets.UTF_8
        val uuid = UUID.nameUUIDFromBytes(string.toByteArray(charSet))
        return uuid.toString()
    }
    fun compressString(string: String): String {
        var tmp :String =""
        if (string.length > 16) {
            tmp = string.substring(0, 8) + string.substring(string.length - 8)
            tmp = tmp.encodeToByteArray().joinToString("") { it.toString(16) }
        } else {
            tmp = string.encodeToByteArray().joinToString("") { it.toString(16) }
        }

        Log.d("compressString", string)
        tmp = tmp.substring(0, 8) + "-" + tmp.substring(8, 12) + "-" + tmp.substring(12, 16) + "-" + tmp.substring(16, 20) + "-" + tmp.substring(20, 32)
        Log.d("compressString", tmp)
        return tmp
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (MainScreen.user_type == "normal") {
            val item = values[position]
            holder.passengerName.text = item.passenger_name
            Log.d("name", values.toString())

            holder.passengerType.text = "탑승 대기"
            holder.passengerTypeColor.setBackgroundColor("#FF4D00".toColorInt())


            if (position == 0 && MainScreen.test_code){
                UserInfoDataContent.onNumber +=1
                holder.passengerType.text = "탑승중"
                holder.passengerTypeColor.setBackgroundColor("#05FF00".toColorInt())
            }
            Log.d("user_uuid", item.passenger_uid.toString())
            if (MainScreen.user_uuid.contains(compressString(item.passenger_uid))){
                holder.passengerType.text = "탑승중"
                UserInfoDataContent.onNumber +=1
                holder.passengerTypeColor.setBackgroundColor("#05FF00".toColorInt())
            }else if (!MainScreen.check_uuid.contains(item.passenger_uid)){
                holder.passengerType.text = "탑승 대기"
                holder.passengerTypeColor.setBackgroundColor("#FF4D00".toColorInt())
            }
            holder.callBtn.setOnClickListener {
                val call = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.passenger_phone))
                ContextCompat.startActivity(holder.itemView.context, call, null)

            }
        }else{
            val item = values[position]
            holder.passengerName.text = item.passenger_name
            holder.passengerType.text = "탑승중"
            UserInfoDataContent.onNumber +=1
            holder.passengerTypeColor.setBackgroundColor("#05FF00".toColorInt())
            holder.callBtn.gone()
        }


    }

    override fun getItemCount(): Int {

        return values.size

    }

    inner class ViewHolder(binding: FragmentUserInfoDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val passengerName: TextView = binding.passengerName
        val passengerType: TextView = binding.passengerType
        val passengerTypeColor: View = binding.passengerTypeColor
        val callBtn: ImageButton = binding.callBtn



    }

}