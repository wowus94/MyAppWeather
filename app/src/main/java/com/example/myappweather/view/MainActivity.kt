package com.example.myappweather.view

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.myappweather.R
import com.example.myappweather.utils.KEY_BUNDLE_WEATHER_ONE
import com.example.myappweather.utils.KEY_MY_ACTION
import com.example.myappweather.view.experiments.MainService
import com.example.myappweather.view.experiments.MyBroadcastReceiver
import com.example.myappweather.view.experiments.ThreadsFragment
import com.example.myappweather.view.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WeatherListFragment.newInstance()).commit()
        }

        startService(Intent(this, MainService::class.java).apply {
            putExtra(KEY_BUNDLE_WEATHER_ONE, "Привет, сервис")
        })

        val receiver = MyBroadcastReceiver()
        registerReceiver(receiver, IntentFilter(KEY_MY_ACTION))
        //LocalBroadcastManager.getInstance(this).registerReceiver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_threads -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ThreadsFragment.newInstance()).commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}