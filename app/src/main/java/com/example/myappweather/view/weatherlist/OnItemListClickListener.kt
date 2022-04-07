package com.example.myappweather.view.weatherlist

import com.example.myappweather.repository.Weather

interface OnItemListClickListener {
    fun onItemClick(weather: Weather)


}