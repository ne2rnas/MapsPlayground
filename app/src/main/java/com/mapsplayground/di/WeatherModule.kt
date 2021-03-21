package com.mapsplayground.di

import com.kwabenaberko.openweathermaplib.constant.Languages
import com.kwabenaberko.openweathermaplib.constant.Units
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.mapsplayground.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    @Provides
    fun provideOpenWeatherMapHelper(): OpenWeatherMapHelper {
        return OpenWeatherMapHelper(BuildConfig.OPEN_WEATHER_MAPS_API_KEY).apply {
            setUnits(Units.METRIC)
            setLanguage(Languages.ENGLISH)
        }
    }
}
