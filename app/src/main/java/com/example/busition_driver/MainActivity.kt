package com.example.busition_driver

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.busition_driver.Gps.LocationTraker
import com.example.busition_driver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)


        val navController = findNavController(R.id.main_nav)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)





        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "1:1 문의사항....", Snackbar.LENGTH_LONG)
//                .setAnchorView(R.id.fab)
//                .setAction("Action", null).show()
//            startService(Intent(this@MainActivity, FloatingService::class.java))
//            startForegroundService(Intent(this@MainActivity, FloatingService::class.java))
//            val lc = LocationTraker(this,this)

        }
        if (!checkOverDisplayPermission()) {
            requestOverDisplayPermission()
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

}