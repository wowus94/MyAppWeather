package com.example.myappweather.view.experiments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.myappweather.utils.KEY_BUNDLE_WEATHER_TWO

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val extra = it.getStringExtra(KEY_BUNDLE_WEATHER_TWO)
            Log.d("@@@", "MyBroadcastReceiver onReceive $extra")
        }
    }
}