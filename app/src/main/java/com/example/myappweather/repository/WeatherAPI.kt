package com.example.myappweather.repository

import com.example.myappweather.repository.dto.WeatherDTO
import com.example.myappweather.utils.LAT
import com.example.myappweather.utils.LON
import com.example.myappweather.utils.YANDEX_API_KEY
import com.example.myappweather.utils.YANDEX_ENDPOINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherAPI {
    @GET(YANDEX_ENDPOINT)
    fun getWeather(
        @Header(YANDEX_API_KEY) apikey: String,
        @Query(LAT) lat: Double,
        @Query(LON) lon: Double
    ): Call<WeatherDTO>
}