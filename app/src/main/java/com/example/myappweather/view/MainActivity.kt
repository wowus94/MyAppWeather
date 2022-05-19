package com.example.myappweather.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.myappweather.MyApp
import com.example.myappweather.R
import com.example.myappweather.lesson10.MapsFragment
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

    companion object {
        private const val NOTIFICATION_ID_LOW = 1
        private const val NOTIFICATION_ID_HIGH = 2
        private const val CHANNEL_ID_LOW = "channel_id_1"
        private const val CHANNEL_ID_HIGH = "channel_id_2"
    }

    private fun push() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilderLow = NotificationCompat.Builder(this, CHANNEL_ID_LOW).apply {
            setSmallIcon(R.drawable.ic_map_pin)
            setContentTitle(getString(R.string.notification_title_low))
            setContentText(getString(R.string.notification_low_text))
            priority = NotificationManager.IMPORTANCE_HIGH
        }

        val notificationBuilderHigh = NotificationCompat.Builder(this, CHANNEL_ID_HIGH).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle(getString(R.string.notification_title_high))
            setContentText(getString(R.string.notification_high_text))
            priority = NotificationManager.IMPORTANCE_HIGH
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameLow = "Name $CHANNEL_ID_LOW"
            val channelDescriptionLow = "Name $CHANNEL_ID_LOW"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow =
                NotificationChannel(CHANNEL_ID_LOW, channelNameLow, channelPriorityLow).apply {
                    description = channelDescriptionLow
                }
            notificationManager.createNotificationChannel(channelLow)
        }
        notificationManager.notify(NOTIFICATION_ID_LOW, notificationBuilderLow.build())


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameHigh = "Name $CHANNEL_ID_HIGH"
            val channelDescriptionHigh = "Name $CHANNEL_ID_HIGH"
            val channelPriorityHigh = NotificationManager.IMPORTANCE_HIGH
            val channelHigh =
                NotificationChannel(CHANNEL_ID_HIGH, channelNameHigh, channelPriorityHigh).apply {
                    description = channelDescriptionHigh
                }
            notificationManager.createNotificationChannel(channelHigh)
        }
        notificationManager.notify(NOTIFICATION_ID_HIGH, notificationBuilderHigh.build())
    }

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

        push()
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

            R.id.action_menu_google_maps -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, MapsFragment())
                    .addToBackStack("").commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}