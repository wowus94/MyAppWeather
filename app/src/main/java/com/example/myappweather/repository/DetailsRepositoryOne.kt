package com.example.myappweather.repository

import com.example.myappweather.viewmodel.DetailsViewModel

interface DetailsRepositoryOne {
    fun getWeatherDetails(city:City, callback: DetailsViewModel.Callback)
}