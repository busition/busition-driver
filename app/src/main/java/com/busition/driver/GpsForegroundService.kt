package com.busition.driver

import android.app.*
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.busition.driver.Gps.LocationTraker
import com.busition.driver.main_screen.FragmentItemDetailScreen
import com.busition.driver.main_screen.MainScreen
import com.busition.driver.main_screen.ScheduleList


class GpsForegroundService : Service() {
    var locationTraker: LocationTraker? = null
    private var mThread: Thread? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "stop" -> {
                Log.d("service", "stop")
                stopSelf()
                stopForeground(true)
            }
            null ->{
                stopSelf()
                stopForeground(true)
            }
            "start" ->{
                generateForegroundNotification()
                gpsWorker()
                SocketIo.startSocket(FragmentItemDetailScreen.route_uuid)


            }


        }
        Log.d("service", "${intent?.action}")


        return START_NOT_STICKY
    }

    private var iconNotification: Bitmap? = null
    private var notification: Notification? = null
    var mNotificationManager: NotificationManager? = null
    private val mNotificationId = 123

    fun isNotificationEnabled(context: Context): Boolean {
        return NotificationManagerCompat.from(context.applicationContext)
            .areNotificationsEnabled()
    }
    private fun generateForegroundNotification() {
        isNotificationEnabled(this)
        iconNotification = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        if (mNotificationManager == null) {
            mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        assert(mNotificationManager != null)
        mNotificationManager?.createNotificationChannelGroup(
            NotificationChannelGroup("chats_group", "Chats")
        )
        val notificationChannel =
            NotificationChannel("service_channel", "Service Notifications",
                NotificationManager.IMPORTANCE_MIN)
        notificationChannel.enableLights(false)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
        mNotificationManager?.createNotificationChannel(notificationChannel)
        val pauseIntent = Intent(this, GpsForegroundService::class.java).apply {
            action = "stop"
        }
        val pausePendingIntent = PendingIntent.getService(this, 0, pauseIntent, FLAG_MUTABLE )

        val builder = NotificationCompat.Builder(this, "service_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Busition Driver Service")
            .setContentText("Busition Driver Service is running")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(androidx.core.R.drawable.notification_bg, "Stop", pausePendingIntent)
            .setAutoCancel(true)

        notification = builder.build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(mNotificationId, notification!!, FOREGROUND_SERVICE_TYPE_LOCATION)
        }else{
            startForeground(mNotificationId, notification!!)
        }

    }

    private fun gpsWorker() {
        locationTraker =
            FragmentItemDetailScreen.gmap?.let {
                LocationTraker(
                    applicationContext,
                    it,
                    FragmentItemDetailScreen.polyList!!
                )
            }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mThread != null) {
            mThread!!.interrupt()
            mThread = null
        }
        locationTraker?.finish()
        locationTraker = null
        SocketIo.closeSocket()
        stopSelf()
        Log.d("service", "onDestroy")
    }
}