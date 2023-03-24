package com.busition.driver

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.busition.driver.Gps.LocationTraker
import com.busition.driver.main_screen.FragmentItemDetailScreen
import com.busition.driver.main_screen.MainScreen

class GpsForegroundService : Service() {
    var locationTraker: LocationTraker? = null
    private var mThread: Thread? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action != null) {
            stopForeground(true)
            stopSelf()
        }
        Log.d("service", "i'm service")
        generateForegroundNotification()
        gpsWorker()
        return START_NOT_STICKY
    }

    private var iconNotification: Bitmap? = null
    private var notification: Notification? = null
    var mNotificationManager: NotificationManager? = null
    private val mNotificationId = 123

    private fun generateForegroundNotification() {
        val intentMainLanding = Intent(this, MainScreen::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intentMainLanding, PendingIntent.FLAG_MUTABLE)
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
        val builder = NotificationCompat.Builder(this, "service_channel")

        builder.setContentTitle(StringBuilder(resources.getString(R.string.app_name)).append(" service is running").toString())
            .setTicker(StringBuilder(resources.getString(R.string.app_name)).append("service is running").toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
        if (iconNotification != null) {
            builder.setLargeIcon(Bitmap.createScaledBitmap(iconNotification!!, 128, 128, false))
        }

        notification = builder.build()
        startForeground(mNotificationId, notification)

    }

    private fun gpsWorker() {
        locationTraker =
            FragmentItemDetailScreen.gmap?.let {
                LocationTraker(
                    applicationContext,
                    it
                )
            }
//        mThread = Thread {
//            while (true) {
//                try {
//                    Thread.sleep(100)
//                    Log.d("service", "i'm service")
//                    Log.d("service", locationTraker?.nowLocation.toString())
//                } catch (e: InterruptedException) {
//                    Log.d("error", e.toString())
//                }
//            }
//        }


    }
    override fun onDestroy() {
        super.onDestroy()
        if (mThread != null) {
            mThread!!.interrupt()
            mThread = null
        }
        stopSelf()
        Log.d("service", "onDestroy")
    }
}