package com.example.myappweather.viewmodel

import com.example.myappweather.repository.Weather

sealed class AppState {
    object Loading : AppState()
    data class Success(val weatherList: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
}