package com.fachmi.miweather.repository

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

}