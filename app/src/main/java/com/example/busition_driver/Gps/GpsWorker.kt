package com.example.busition_driver.Gps

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class GpsWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        return try {
            Log.d("gps", "gps")
            Thread.sleep(3)
            Result.success()
        }catch (e:Exception){
            Result.failure()
    }
}
}