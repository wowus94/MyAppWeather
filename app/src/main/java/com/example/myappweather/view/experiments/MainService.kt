package com.example.myappweather.view.experiments

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.myappweather.utils.*
import java.lang.Thread.sleep

class MainService(val name: String = "") : IntentService(name) {
    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {
        Log.d("@@@", "work MainService")
        intent?.let {
            val extra = it.getStringExtra(KEY_BUNDLE_WEATHER_ONE)
            Log.d("@@@", "work MainService $extra")
            sleep(1000L)
            val message = Intent(KEY_MY_ACTION)
            message.putExtra(KEY_BUNDLE_WEATHER_TWO, "Привет, активити и тебе всего хорошего")
            sendBroadcast(message)
        }
    }
}