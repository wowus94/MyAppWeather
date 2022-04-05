package com.example.myappweather.repository

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorageWorld(): List<Weather>
    fun getWeatherFromLocalStorageRus(): List<Weather>
}