package com.fachmi.miweather.response

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @field:SerializedName("visibility") val visibility: Int = 0,
    @field:SerializedName("timezone") val timezone: Int = 0,
    @field:SerializedName("main") val main: Main = Main(),
    @field:SerializedName("clouds") val clouds: Clouds = Clouds(),
    @field:SerializedName("sys") val sys: Sys = Sys(),
    @field:SerializedName("dt") val dt: Int = 0,
    @field:SerializedName("coord") val coord: Coord = Coord(),
    @field:SerializedName("weather") val weather: List<WeatherItem> = listOf(),
    @field:SerializedName("name") val name: String = "",
    @field:SerializedName("cod") val cod: Int = 0,
    @field:SerializedName("id") val id: Int = 0,
    @field:SerializedName("base") val base: String = "",
    @field:SerializedName("wind") val wind: Wind = Wind()
)

data class Sys(
    @field:SerializedName("country") val country: String = "",
    @field:SerializedName("sunrise") val sunrise: Int = 0,
    @field:SerializedName("sunset") val sunset: Int = 0,
    @field:SerializedName("id") val id: Int = 0,
    @field:SerializedName("type") val type: Int = 0
)

data class Main(
    @field:SerializedName("temp") val temp: Double = 0.0,
    @field:SerializedName("temp_min") val tempMin: Double = 0.0,
    @field:SerializedName("humidity") val humidity: Int = 0,
    @field:SerializedName("pressure") val pressure: Int = 0,
    @field:SerializedName("feels_like") val feelsLike: Double = 0.0,
    @field:SerializedName("temp_max") val tempMax: Double = 0.0
)

data class WeatherItem(
    @field:SerializedName("icon") val icon: String = "",
    @field:SerializedName("description") val description: String = "",
    @field:SerializedName("main") val main: String = "",
    @field:SerializedName("id") val id: Int = 0
)

data class Clouds(
    @field:SerializedName("all") val all: Int = 0
)

data class Coord(
    @field:SerializedName("lon") val lon: Double = 0.0,
    @field:SerializedName("lat") val lat: Double = 0.0
)

data class Wind(
    @field:SerializedName("deg") val deg: Double = 0.0,
    @field:SerializedName("speed") val speed: Double = 0.0
)
