package com.example.myappweather.viewmodel

import com.example.myappweather.repository.Weather

sealed class DetailsState {
    object Loading : DetailsState()
    data class Success(val weather: Weather) : DetailsState()
    data class Error(val error: Throwable) : DetailsState()
}