package com.mapsplayground.view.map

data class HarborInfo(
    val harborName: String,
    val weatherIcon: WeatherIcon,
    val description: String?,
    val temp: String,
    val feelsLike: String,
    val humidity: String,
    val pressure: String
)

sealed class WeatherIcon {
    object Thunderstorm : WeatherIcon()
    object Drizzle : WeatherIcon()
    object Rain : WeatherIcon()
    object Snow : WeatherIcon()
    object Atmosphere : WeatherIcon()
    object Clear : WeatherIcon()
    object Clouds : WeatherIcon()
    object Unknown : WeatherIcon()
}
