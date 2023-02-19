package com.example.busition_driver

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Button
import com.example.busition_driver.databinding.FloatingViewBinding

class FloatingService: Service() {
    private lateinit var floatView : ViewGroup
    private lateinit var windowManager: WindowManager
    private lateinit var floatWindowLayoutParam: WindowManager.LayoutParams
    private lateinit var exitBtn: Button
    private lateinit var binding: FloatingViewBinding
    private  var layoutType: Int = 0



    override fun onCreate() {
        super.onCreate()
        val metrics: DisplayMetrics = applicationContext.resources.displayMetrics
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        binding = FloatingViewBinding.inflate(LayoutInflater.from(this))

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val inflater = baseContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        floatView = inflater.inflate(R.layout.floating_view, null) as ViewGroup
        exitBtn = binding.exitBtn
        layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

        floatWindowLayoutParam = WindowManager.LayoutParams(
            (width * 0.55f).toInt(),
            (height * 0.58f).toInt(),
            layoutType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        floatWindowLayoutParam.gravity = Gravity.CENTER
        floatWindowLayoutParam.x= 0
        floatWindowLayoutParam.y= 0

        windowManager.addView(floatView, floatWindowLayoutParam)

        Log.d("FloatingService", "onCreate: ")
        exitBtn.setOnClickListener {
            stopSelf()
            windowManager.removeView(floatView)

        }


        //Another feature of the floating window is, the window is movable.
        //The window can be moved at any position on the screen.
        floatView.setOnTouchListener(object : OnTouchListener {
            val floatWindowLayoutUpdateParam = floatWindowLayoutParam
            var x = 0.0
            var y = 0.0
            var px = 0.0
            var py = 0.0
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = floatWindowLayoutUpdateParam.x.toDouble()
                        y = floatWindowLayoutUpdateParam.y.toDouble()
                        //returns the original raw X coordinate of this event
                        px = event.rawX.toDouble()
                        //returns the original raw Y coordinate of this event
                        py = event.rawY.toDouble()
                    }
                    MotionEvent.ACTION_MOVE -> {
                        floatWindowLayoutUpdateParam.x = (x + event.rawX - px).toInt()
                        floatWindowLayoutUpdateParam.y = (y + event.rawY - py).toInt()

                        //updated parameter is applied to the WindowManager
                        windowManager.updateViewLayout(floatView, floatWindowLayoutUpdateParam)
                    }
                }
                return false
            }
        })

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}