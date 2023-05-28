package com.busition.driver

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.busition.driver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController = findNavController(R.id.main_nav)
        appBarConfiguration = AppBarConfiguration(navController.graph)

//        LocationPermissionDialog().show(supportFragmentManager, "locationPermissionDialog")
        if (!checkOverDisplayPermission()) {
            requestOverDisplayPermission()
            AlertDialog.Builder(this)
                .setTitle("위치 정보 수집에 동의")
                .setMessage("Busition Driver 앱은 앱이 종료되었거나 사용 중이 아닐 때도 위치 데이터를 수집하여 버스 실시간 위치 정보 제공 및 승하차 자동 기능을 제공합니다.\n" +

                        "위치 정보 수집에 동의하지 않으시면 Busition Driver 앱을 사용하실 수 없습니다.\n" + "위치 정보 수집에 동의하시겠습니까?")
                .setPositiveButton("동의", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {

                    }
                })
                .setNegativeButton("거부", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        finish()
                    }
                })
                .create()
                .show()
        }




        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "1:1 문의사항....", Snackbar.LENGTH_LONG)
//                .setAnchorView(R.id.fab)
//                .setAction("Action", null).show()
//            startService(Intent(this, FloatingWindowGFG::class.java))
            startService(Intent(this, FloatingService::class.java))
//            startForegroundService(Intent(this@MainActivity, FloatingService::class.java))
//            finish()
//            val lc = LocationTraker(this,this)

        }



    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_screen_nav)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    private fun requestOverDisplayPermission() {
        val intent =
            Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))

        val getResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                Log.d("permission result", result.resultCode.toString())
                if (!checkOverDisplayPermission()) {
                    finish()
                }
            }
        AlertDialog.Builder(this)
            .setTitle("권한 요청")
            .setMessage("권한을 허용하지 않으면 앱을 사용할 수 없습니다.")
            .setPositiveButton("확인") { dialog, which ->
                getResult.launch(intent)
            }
            .setNegativeButton("취소") { dialog, which ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun gpsPermissionRequest() {


        // 권한 체크해서 권한이 있을 때
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("permission", "권한 있음")
        }

        // 권한이 없을 때 권한을 요구함
        else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ),
                    1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    1
                )
            }

        }

    }

    private fun checkOverDisplayPermission(): Boolean {
        Log.d("permission", Settings.canDrawOverlays(this).toString())
        return Settings.canDrawOverlays(this)
    }
    companion object {
        var type = 0
    }
}