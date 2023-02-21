package com.example.busition_driver.main_screen

import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.busition_driver.FloatingService
import com.example.busition_driver.R
import com.example.busition_driver.databinding.ActivityMainScreenBinding
import com.google.android.material.snackbar.Snackbar

class MainScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        //The ACTIVITY_SERVICE is needed to retrieve a ActivityManager for interacting with the global system
        //It has a constant String value "activity".
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        //A loop is needed to get Service information that are currently running in the System.
        //So ActivityManager.RunningServiceInfo is used. It helps to retrieve a
        //particular service information, here its this service.
        //getRunningServices() method returns a list of the services that are currently running
        //and MAX_VALUE is 2147483647. So at most this many services can be returned by this method.
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            //If this service is found as a running, it will return true or else false.
            if (FloatingService::class.java.getName() == service.service.className) {
                return true
            }
        }
        return false
    }

}