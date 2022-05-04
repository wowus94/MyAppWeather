package com.example.myappweather.repository

import com.example.myappweather.repository.dto.WeatherDTO
import com.example.myappweather.viewmodel.DetailsViewModel

interface DetailsRepository {
    fun getWeatherDetails(city:City, callback: DetailsViewModel.Callback)
}