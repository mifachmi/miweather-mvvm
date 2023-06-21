package com.fachmi.miweather.di

import android.content.Context
import com.fachmi.miweather.network.ApiConfig
import com.fachmi.miweather.repository.CurrentWeatherRepository
import com.fachmi.miweather.response.RemoteDataSource

object Injection {

    fun provideCurrentWeatherRepository(context: Context): CurrentWeatherRepository {
        val apiService = ApiConfig.provideApiService(context)
        val remoteDataSource = RemoteDataSource.getInstance(apiService)
        return CurrentWeatherRepository.getInstance(remoteDataSource)
    }
}