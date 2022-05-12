package com.example.myappweather.repository

import com.example.myappweather.MyApp
import com.example.myappweather.utils.convertHistoryEntityToWeather
import com.example.myappweather.utils.convertWeatherToEntity
import com.example.myappweather.viewmodel.DetailsViewModel
import com.example.myappweather.viewmodel.HistoryViewModel


class DetailsRepositoryRoomImpl:DetailsRepositoryOne,DetailsRepositoryAll,DetailsRepositoryAdd {
    override fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll) {
        callback.onResponse(convertHistoryEntityToWeather(MyApp.getHistoryDao().getAll()))
    }

    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        val list =convertHistoryEntityToWeather(MyApp.getHistoryDao().getHistoryForCity(city.name))
        if(list.isEmpty()){
            callback.onFail() // то и отобразить нечего
        }else{
            callback.onResponse(list.last()) // FIXME hack
        }

    }

    override fun addWeather(weather: Weather) {
        MyApp.getHistoryDao().insert(convertWeatherToEntity(weather))
    }

}