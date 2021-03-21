package com.mapsplayground.view.map.usecase

import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.implementation.callback.CurrentWeatherCallback
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import com.mapsplayground.utils.result.ResultOf
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val openWeatherMapHelper: OpenWeatherMapHelper
) {

    operator fun invoke(lat: Double, lon: Double): Single<ResultOf<CurrentWeather>> {
        return Single.create { emitter ->
            val listener = object : CurrentWeatherCallback {
                override fun onSuccess(currentWeather: CurrentWeather) {
                    if (emitter.isDisposed) return
                    emitter.onSuccess(ResultOf.Success(currentWeather))
                }

                override fun onFailure(throwable: Throwable?) {
                    if (emitter.isDisposed) return
                    emitter.onSuccess(ResultOf.Error(throwable))
                }
            }
            openWeatherMapHelper.getCurrentWeatherByGeoCoordinates(lat, lon, listener)
        }
    }
}
