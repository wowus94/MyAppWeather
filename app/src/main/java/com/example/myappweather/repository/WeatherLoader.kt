package com.example.myappweather.repository

import android.os.Handler
import android.os.Looper
import com.example.myappweather.BuildConfig
import com.example.myappweather.repository.dto.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherLoader(private val onServerResponseListener: OnServerResponse) {
    fun loaderWeather(lat: Double, lon: Double) {

        val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
        val url = URL(urlText)
        val urlConnection: HttpURLConnection =
            (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 1000
                readTimeout = 1000
                addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
            }

        Thread {
            try {

                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))

                val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                Handler(Looper.getMainLooper()).post {
                    onServerResponseListener.onResponse(
                        weatherDTO
                    )
                }
            } catch (e: Exception) {
                //TODO HW "что-то пошло не так" Snackbar
            } finally {
                urlConnection.disconnect()
            }
        }.start()
    }
}