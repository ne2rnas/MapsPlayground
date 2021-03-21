package com.mapsplayground.view.map.usecase

import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.implementation.callback.CurrentWeatherCallback
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import com.mapsplayground.utils.result.ResultOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetWeatherUseCaseTest {

    private val openWeatherMapHelper = mockk<OpenWeatherMapHelper>()

    private lateinit var useCase: GetWeatherUseCase

    private lateinit var observer: TestObserver<ResultOf<CurrentWeather>>

    private val slot = slot<CurrentWeatherCallback>()

    @BeforeEach
    fun setUp() {
        every {
            openWeatherMapHelper.getCurrentWeatherByGeoCoordinates(
                any(),
                any(),
                capture(slot)
            )
        } answers { }
        useCase = GetWeatherUseCase(openWeatherMapHelper)
    }

    @Test
    fun `observed current weather`() {
        observer = useCase(1.0, 2.0).test()
        val expectedResult = mockk<CurrentWeather>()
        slot.captured.onSuccess(expectedResult)
        observer.assertResult(ResultOf.Success(expectedResult))
    }

    @Test
    fun `observed error`() {
        observer = useCase(1.0, 2.0).test()
        val expectedResult = Throwable()
        slot.captured.onFailure(expectedResult)
        observer.assertResult(ResultOf.Error(expectedResult))
    }
}
