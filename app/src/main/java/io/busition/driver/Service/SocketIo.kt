package io.busition.driver.Service

import android.os.Handler
import android.os.Looper
import android.util.Log
import io.busition.driver.main_screen.recycler_view.StationItemRecyclerAdapter
import io.busition.driver.data.passenger_list.PassengerData
import io.busition.driver.main_screen.fragment.BusStartMainScreen
import io.busition.driver.main_screen.dataholder.StationDataContent
import io.busition.driver.main_screen.dataholder.UserInfoDataContent
import io.busition.driver.main_screen.fragment.UserInfoDataFragment
import io.busition.driver.roomDB.DataManager
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject


object SocketIo {
    private var socketIo: SocketIo? = null
    private lateinit var socketServer: Socket

    fun startSocket(id: String) {
        socketIo = SocketIo
        try {
//            val pack = JSONObject()
//            pack.put("id", id)
            val tmp = JSONObject()
            tmp.put("route_id", id)
            tmp.put("user_id", "123")
            socketServer = IO.socket("https://socket.dev2.busition.com/user")
            socketServer.connect()
            Log.d("socket type", socketServer.toString())
            Log.d("socket pack jsonObject", tmp.toString())

            socketServer.emit("open_room", tmp.toString())
//            recvLocation()
//            recvClose()
            recvUserUuid()

        } catch (e: Exception) {
            Log.d("socket error", e.toString())
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
                        BusStartMainScreen.stationItemRecyclerAdapter.notifyDataSetChanged()
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
        socketServer.emit("company user", tmp)
        Log.d("socket check_passenger", tmp.toString())
        BusStartMainScreen.check_passenger_list.add(uuid)

    }

    fun recvUserUuid(){
        try {
            val onMessage = Emitter.Listener { args ->
                val obj = JSONObject(args[0].toString())
                val uuid:String = obj.getString("uuid")
                val stationUuid:String = obj.getString("station uuid")
                val name:String = obj.getString("name")
                UserInfoDataContent.addItem(PassengerData(uuid, name, "", "", stationUuid))
                DataManager.addPassengerForStation(stationUuid, uuid, name, "")
                UserInfoDataFragment.userInfoRecyclerAdapter.notifyDataSetChanged()
                Log.d("socket", obj.toString())

            }
            socketServer.on("user uuid", onMessage)
        } catch (e: Exception) {
            Log.d("socket recv error", e.toString())
        }
    }
    fun sendLocation(lat: Double, lon: Double, speed: Float) {
        val tmp = JSONObject()
        tmp.put("latitude", lat)
        tmp.put("longitude", lon)
        tmp.put("speed", speed)
        tmp.put("stationName", StationDataContent.ITEMS.get(StationDataContent.ind).station_name)
        Log.d("socket location jsonObject", tmp.toString())
        Log.d("socket type", socketServer.toString())
        socketServer.emit("send_location", tmp)
        Log.d("socket send location", tmp.toString())
    }



    fun closeSocket(id: String) {
        val tmp = JSONObject()
        tmp.put("route_id", id)
        Log.d("close socket", tmp.toString())
        socketServer.emit("exit", tmp)
        socketServer.disconnect()
        socketServer.close()
    }
}