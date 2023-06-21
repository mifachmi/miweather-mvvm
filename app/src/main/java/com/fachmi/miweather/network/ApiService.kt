package com.fachmi.miweather.network

import com.fachmi.miweather.response.CurrentWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    fun getCurrentWeather(
        @Query("q") cityName: String
    ): Call<CurrentWeatherResponse>
}