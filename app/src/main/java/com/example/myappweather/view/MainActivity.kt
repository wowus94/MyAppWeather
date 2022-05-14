package com.example.myappweather.view

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.myappweather.MyApp
import com.example.myappweather.R
import com.example.myappweather.lesson9.WorkWithContentProviderFragment
import com.example.myappweather.utils.KEY_BUNDLE_WEATHER_ONE
import com.example.myappweather.utils.KEY_SP_FILE_NAME_1
import com.example.myappweather.utils.KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN
import com.example.myappweather.utils.KEY_WAVE_MY_ACTION
import com.example.myappweather.view.experiments.MainService
import com.example.myappweather.view.experiments.MyBroadcastReceiver
import com.example.myappweather.view.experiments.ThreadsFragment
import com.example.myappweather.view.historylist.HistoryWeatherListFragment
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
        registerReceiver(receiver, IntentFilter(KEY_WAVE_MY_ACTION))


        val sp = getSharedPreferences(KEY_SP_FILE_NAME_1, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, true)
        editor.apply()

        val defaultValueIsRussian = true
        sp.getBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, defaultValueIsRussian)


        MyApp.getHistoryDao().getAll()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_threads -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ThreadsFragment.newInstance()).addToBackStack("")
                    .commit()
            }

            R.id.action_history -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, HistoryWeatherListFragment.newInstance())
                    .addToBackStack("").commit()
            }

            R.id.action_work_with_content_provider -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, WorkWithContentProviderFragment.newInstance())
                    .addToBackStack("").commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}