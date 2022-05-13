package com.example.myappweather.repository

import com.example.myappweather.BuildConfig
import com.example.myappweather.repository.dto.WeatherDTO
import com.example.myappweather.utils.YANDEX_API_KEY
import com.example.myappweather.utils.YANDEX_DOMAIN
import com.example.myappweather.utils.YANDEX_ENDPOINT
import com.example.myappweather.utils.convertDtoToModel
import com.example.myappweather.viewmodel.DetailsViewModel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

class DetailsRepositoryOneOkHttp2Impl:DetailsRepositoryOne {
    override fun getWeatherDetails(city: City,callback: DetailsViewModel.Callback) {
        val client = OkHttpClient()
        val builder = Request.Builder()
        builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url("$YANDEX_DOMAIN${YANDEX_ENDPOINT}lat=${city.lat}&lon=${city.lon}")
        val request = builder.build()
        val call = client.newCall(request)
        Thread{
            val response = call.execute()
            if(response.isSuccessful){
                val serverResponse = response.body()!!.string()
                val weatherDTO: WeatherDTO = Gson().fromJson(serverResponse, WeatherDTO::class.java)
                val weather = convertDtoToModel(weatherDTO)
                weather.city = city
                callback.onResponse(weather)
            }else{
                //TODO HW
            }
        }.start()
    }
}