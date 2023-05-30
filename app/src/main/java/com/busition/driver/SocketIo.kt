package com.busition.driver

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.busition.driver.main_screen.FragmentItemDetailScreen
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject


object SocketIo {
    private var socketIo: SocketIo? = null
    private lateinit var socketServer: Socket

    fun startSocket(id: String, type: String) {
        socketIo = SocketIo
        try {
//            val pack = JSONObject()
//            pack.put("id", id)
            val tmp = JSONObject()
            tmp.put("route_id", id)
            tmp.put("type", type)
            socketServer = IO.socket("http://api.dev.busition.com:3000/user")
            socketServer.connect()
            Log.d("socket pack jsonObject", tmp.toString())

            socketServer.emit("open_room", tmp.toString())
//            recvLocation()
//            recvClose()

        } catch (e: Exception) {
            Log.d("socket", e.toString())
        }

    }

    fun recvLocation() {
        try {
            val onMessage = Emitter.Listener { args ->
                val obj = JSONObject(args[0].toString())
                if (StationItemRecyclerAdapter.delay != obj.getInt("delayed")) {
                    val handler = Handler(Looper.getMainLooper())
                    handler.post(Runnable {
                        StationItemRecyclerAdapter.delay = obj.getInt("delayed")
                        FragmentItemDetailScreen.stationItemRecyclerAdapter.notifyDataSetChanged()
                    })

                }

                Log.d("socket", obj.toString())

            }
            socketServer.on("data", onMessage)
        } catch (e: Exception) {
            Log.d("socket recv error", e.toString())
        }

    }


    fun sendUserUuid(uuid: String, id: String) {
        val tmp = JSONObject()
        tmp.put("uuid", uuid)
        tmp.put("id", id)
        socketServer.emit("check_passenger", tmp)
        Log.d("socket check_passenger", tmp.toString())
        FragmentItemDetailScreen.check_passenger_list.add(uuid)
    }

    fun sendLocation(lat: Double, lon: Double, speed: Float) {
        val tmp = JSONObject()
        tmp.put("latitude", lat)
        tmp.put("longitude", lon)
        tmp.put("speed", speed)
        Log.d("socket location jsonObject", tmp.toString())
        socketServer.emit("send_location", tmp)
        Log.d("socket send location", tmp.toString())
    }



    fun closeSocket(id: String, type: String) {
        val tmp = JSONObject()
        tmp.put("route_id", id)
        tmp.put("type", type)
        Log.d("close socket", tmp.toString())
        socketServer.emit("exit", tmp)
        socketServer.disconnect()
        socketServer.close()
    }
}