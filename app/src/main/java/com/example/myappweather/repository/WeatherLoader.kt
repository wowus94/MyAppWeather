package com.example.myappweather.repository

import android.os.Handler
import android.os.Looper
import com.example.myappweather.BuildConfig
import com.example.myappweather.repository.dto.WeatherDTO
import com.example.myappweather.utils.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherLoader( private val onServerResponseListener: OnServerResponse) {
    fun loaderWeather(lat: Double, lon: Double) {

        val urlText = "$YANDEX_DOMAIN${YANDEX_ENDPOINT}lat=$LAT&lon=$LON"
        val uri = URL(urlText)

        Thread {
            val urlConnection: HttpURLConnection =
                (uri.openConnection() as HttpURLConnection).apply {
                    connectTimeout = 1000
                    readTimeout = 1000
                    addRequestProperty(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
                }

            try {

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
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                        Handler(Looper.getMainLooper()).post {
                            onServerResponseListener.onResponse(weatherDTO)
                        }
                    }
                }

            } catch (e: JsonSyntaxException) {

            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }
}