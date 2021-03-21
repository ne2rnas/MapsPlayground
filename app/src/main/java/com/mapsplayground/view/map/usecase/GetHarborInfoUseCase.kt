package com.mapsplayground.view.map.usecase

import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import com.mapsplayground.view.map.HarborInfo
import com.mapsplayground.view.map.WeatherIcon
import javax.inject.Inject

class GetHarborInfoUseCase @Inject constructor() {

    operator fun invoke(harborName: String, currentWeather: CurrentWeather): HarborInfo {
        val weatherIcon = currentWeather.weather.firstOrNull()?.id?.let {
            when (it) {
                in 200..232 -> {
                    WeatherIcon.Thunderstorm
                }
                in 300..321 -> {
                    WeatherIcon.Drizzle
                }
                in 500..531 -> {
                    WeatherIcon.Rain
                }
                in 600..622 -> {
                    WeatherIcon.Snow
                }
                in 701..781 -> {
                    WeatherIcon.Atmosphere
                }
                in 800..800 -> {
                    WeatherIcon.Clear
                }
                in 801..804 -> {
                    WeatherIcon.Clouds
                }
                else -> {
                    WeatherIcon.Unknown
                }
            }
        } ?: WeatherIcon.Unknown
        val description = currentWeather.weather.firstOrNull()?.description ?: ""
        val temp = currentWeather.main.temp.toString()
        val feelsLike = currentWeather.main.feelsLike.toString()
        val humidity = currentWeather.main.humidity.toString()
        val pressure = currentWeather.main.pressure.toString()
        return HarborInfo(
            harborName,
            weatherIcon,
            description,
            temp,
            feelsLike,
            humidity,
            pressure
        )
    }
}
