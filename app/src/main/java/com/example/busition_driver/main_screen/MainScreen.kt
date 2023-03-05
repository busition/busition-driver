package com.example.busition_driver.main_screen

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.busition_driver.FloatingService
import com.example.busition_driver.R
import com.example.busition_driver.databinding.ActivityMainScreenBinding
import com.example.busition_driver.ui.login.LoggedInUserView
import com.example.busition_driver.ui.login.LoginViewModel
import com.example.busition_driver.ui.login.LoginViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates

class MainScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var userName: String


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        userName = intent.getStringExtra("userName").toString()
        driver_id = intent.getIntExtra("driver_id", 0)
        ScheduleList.getUserData(userName, driver_id)

        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        val navController = findNavController(R.id.main_screen_nav)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)





        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "1:1 문의사항....", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
        if(isMyServiceRunning()){
            stopService(Intent(this, FloatingService::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.main_screen_nav)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    private fun isMyServiceRunning(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (FloatingService::class.java.getName() == service.service.className) {
                return true
            }
        }
        return false
    }

    companion object {
        var driver_id: Int = 0
    }

}