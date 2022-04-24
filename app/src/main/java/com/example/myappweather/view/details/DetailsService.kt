package com.example.myappweather.view.details

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.myappweather.BuildConfig
import com.example.myappweather.repository.dto.WeatherDTO
import com.example.myappweather.utils.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DetailsService(val name: String = "") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?)
    {
        Log.d("@@@", "work MainService")
        intent?.let {
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT, 0.0)
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON, 0.0)
            Log.d("@@@", "work $lat $lon")
            val urlText = "$YANDEX_DOMAIN${YANDEX_PATH}lat=$lat&lon=$lon"
            val url = URL(urlText)
            val urlConnection: HttpURLConnection =
                (url.openConnection() as HttpURLConnection).apply {
                    connectTimeout = 1000
                    readTimeout = 1000
                    addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
                }

            val headers = urlConnection.headerFields
            val responseCode = urlConnection.responseCode
            val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))

            val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)

            try {
                Thread.sleep(500)
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage

                val serverside = 500..599
                val clientside = 400..499
                val responseOk = 200..299
                when (responseCode) {
                    in serverside -> {
                    }
                    in clientside -> {
                    }
                    in responseOk -> {
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        //val result = (buffer)
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                    }
                }

            } catch (e: JsonSyntaxException) {

            } finally {
                urlConnection.disconnect()
            }

            val message = Intent(KEY_WAVE_MY_ACTION)
            message.putExtra(
                KEY_BUNDLE_SERVICE_BROADCAST_WEATHER, weatherDTO
            )
            LocalBroadcastManager.getInstance(this).sendBroadcast(message)
        }
    }
}