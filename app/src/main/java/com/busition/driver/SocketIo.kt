package com.busition.driver

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject


object SocketIo {
    private var socketIo :SocketIo? = null
    private lateinit var socketServer:Socket

    fun startSocket(id:String){
        socketIo = SocketIo
        try {
            val pack = JSONObject()
            pack.put("id", id)
            socketServer = IO.socket("http://api.dev.busition.com:3000/driver")
            socketServer.connect()
            Log.d("socket pack jsonObject", pack.toString())
            Log.d("socket", socketServer.toString())
            socketServer.emit("send_id", pack)

        }catch (e:Exception){
            Log.d("socket",e.toString())
        }

    }

    fun recvLocation(){
        try {
            val onMessage = Emitter.Listener { args ->
                val obj = JSONObject(args[0].toString())
                Log.d("socket", obj.toString())
            }
            socketServer.on("data", onMessage)
        }catch (e:Exception){
            Log.d("socket recv error", e.toString())
        }

    }

    fun sendLocation(lat:String, lon:String){
        val tmp =JSONObject()
        tmp.put("latitude", lat)
        tmp.put("longitude", lon)
        Log.d("socket location jsonObject", tmp.toString())
        socketServer.emit("send_location", tmp)
        Log.d("socket send location", tmp.toString())
    }

    fun closeSocket(){
        socketServer.close()
    }

}