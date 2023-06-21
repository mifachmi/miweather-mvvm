package com.fachmi.miweather.model

data class OtherLocationWeatherModel(
    val city: String = "",
    val dateTime: String = "",
    val temperature: Double = 0.0,
    val description: String = "",
    val iconUrl: String = ""
)
