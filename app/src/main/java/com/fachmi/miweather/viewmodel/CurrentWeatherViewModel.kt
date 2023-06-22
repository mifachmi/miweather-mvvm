package com.fachmi.miweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fachmi.miweather.repository.CurrentWeatherRepository
import com.fachmi.miweather.response.ApiResponse
import com.fachmi.miweather.response.CurrentWeatherResponse

class CurrentWeatherViewModel(private val currentWeatherRepository: CurrentWeatherRepository) :
    ViewModel() {

    fun getCurrentWeather(lat: Double, lon: Double): LiveData<ApiResponse<CurrentWeatherResponse>> {
        return currentWeatherRepository.getCurrentWeather(lat, lon)
    }

    fun getCurrentWeatherByCityName(cityName: String): LiveData<ApiResponse<CurrentWeatherResponse>> {
        return currentWeatherRepository.getCurrentWeatherByCity(cityName)
    }

}