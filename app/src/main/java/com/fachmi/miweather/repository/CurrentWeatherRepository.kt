package com.fachmi.miweather.repository

import androidx.lifecycle.LiveData
import com.fachmi.miweather.response.ApiResponse
import com.fachmi.miweather.response.CurrentWeatherResponse
import com.fachmi.miweather.response.RemoteDataSource

class CurrentWeatherRepository private constructor(
    private val remoteDataSource: RemoteDataSource
) {

    companion object {
        @Volatile
        private var instance: CurrentWeatherRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource
        ): CurrentWeatherRepository {
            return instance ?: synchronized(this) {
                instance ?: CurrentWeatherRepository(remoteDataSource)
            }
        }
    }

    fun getCurrentWeather(lat: Double, lon: Double): LiveData<ApiResponse<CurrentWeatherResponse>> {
        return remoteDataSource.getCurrentWeather(lat, lon)
    }

    fun getCurrentWeatherByCity(cityName: String): LiveData<ApiResponse<CurrentWeatherResponse>> {
        return remoteDataSource.getCurrentWeatherByCity(cityName)
    }

}