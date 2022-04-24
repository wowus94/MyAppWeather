package com.example.myappweather.view.experiments

import android.app.IntentService
import android.content.Intent
import android.util.Log

class MainService(val name: String = "") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        Log.d("@@@", "work MainService")
    }
}