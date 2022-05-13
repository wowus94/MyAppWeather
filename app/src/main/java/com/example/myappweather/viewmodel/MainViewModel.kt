package com.example.myappweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myappweather.repository.RepositoryImpl

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: RepositoryImpl = RepositoryImpl()

) :
    ViewModel() {

    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeatherRussia() = getWeather(true)
    fun getWeatherWorld() = getWeather(false)


    private fun getWeather(isRussian: Boolean) {
        Thread {
            liveData.postValue(AppState.Loading)
            val answer =
                if (!isRussian) repository.getWeatherFromLocalStorageWorld() else repository.getWeatherFromLocalStorageRus()
            liveData.postValue(AppState.Success(answer))
        }.start()
    }
}