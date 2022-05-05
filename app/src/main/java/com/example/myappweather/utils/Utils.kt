package com.example.myappweather.utils

import com.example.myappweather.repository.Weather
import com.example.myappweather.repository.dto.FactDTO
import com.example.myappweather.repository.dto.WeatherDTO
import com.example.myappweather.repository.getDefaultCity

const val KEY_BUNDLE_WEATHER_ONE = "key1"
const val KEY_BUNDLE_WEATHER_TWO = "key2"
const val KEY_WAVE_MY_ACTION = "myaction"
const val KEY_BUNDLE_LAT = "lat1"
const val KEY_BUNDLE_LON = "lon1"
const val KEY_BUNDLE_SERVICE_BROADCAST_WEATHER = "weather_BR"
const val KEY_WAVE_SERVICE_BROADCAST = "myaction_way"
const val YANDEX_DOMAIN = "https://api.weather.yandex.ru/"
const val YANDEX_ENDPOINT = "v2/informers?"
const val YANDEX_API_KEY = "X-Yandex-API-Key"
const val KEY_BUNDLE_WEATHER = "weather"
const val LAT = "lat"
const val LON = "lon"


fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.factDTO
    return (Weather(getDefaultCity(), fact.temperature, fact.feelsLike, fact.icon))
}