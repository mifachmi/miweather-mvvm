package com.fachmi.miweather.model

data class NecessaryWeatherInfoModel(
    val city: String = "",
    val dateTime: String = "",
    val humidity: String = "",
    val feelsLike: String = "",
    val sunriseTime: String = "",
    val sunsetTime: String = "",
    val temperature: String = "",
    val lowTemperature: String = "",
    val highTemperature: String = "",
    val visibility: String = "",
    val description: String = "",
    val icon: String = ""
)