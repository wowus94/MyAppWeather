package com.example.myappweather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myappweather.repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repositoryOne: DetailsRepositoryOne = DetailsRepositoryOneRetrofit2Impl(),
    private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryRoomImpl()
) : ViewModel() {

    fun getLiveData() = liveData

    fun getWeather(city: City) {
        liveData.postValue(DetailsState.Loading)
        repositoryOne.getWeatherDetails(city, object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
                repositoryAdd.addWeather(weather)
            }

            override fun onFail() {
                //TODO HW   liveData.postValue(DetailsState.Error())
            }

        })
    }

    interface Callback {
        fun onResponse(weather: Weather)
        fun onFail()
        // TODO HW Fail
    }

    interface CallbackForAll {
        fun onResponse(weather: List<Weather>)
        fun onFail()
        // TODO HW Fail
    }

}