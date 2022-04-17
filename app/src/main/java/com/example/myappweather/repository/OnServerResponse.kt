package com.example.myappweather.repository

import com.example.myappweather.repository.dto.WeatherDTO

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}