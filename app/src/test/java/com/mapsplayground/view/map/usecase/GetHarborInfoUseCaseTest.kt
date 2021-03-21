package com.mapsplayground.view.map.usecase

import com.kwabenaberko.openweathermaplib.model.common.Weather
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import com.mapsplayground.view.map.HarborInfo
import com.mapsplayground.view.map.WeatherIcon
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GetHarborInfoUseCaseTest {

    private lateinit var useCase: GetHarborInfoUseCase
    private lateinit var result: HarborInfo

    @BeforeEach
    fun setUp() {
        useCase = GetHarborInfoUseCase()
    }

    @Nested
    inner class HarborInfoThunderstorm {

        private val harborName = "test"
        private val description = "hello world"
        private val temp = 1.0
        private val feelsLike = 2.0
        private val humidity = 3.0
        private val pressure = 4.0

        @BeforeEach
        fun setUp() {
            val weatherThunderstorm = mockWeather(200, description)
            val currentWeather =
                mockCurrentWeather(weatherThunderstorm, temp, feelsLike, humidity, pressure)
            result = useCase(harborName, currentWeather)
        }

        @Test
        fun `thunderstorm harbor info is created`() {
            val expected = HarborInfo(
                harborName,
                WeatherIcon.Thunderstorm,
                description,
                temp.toString(),
                feelsLike.toString(),
                humidity.toString(),
                pressure.toString()
            )
            assertEquals(expected, result)
        }
    }

    @Nested
    inner class HarborInfoDrizzle {

        private val harborName = "test 2"
        private val description = "hello world 2"
        private val temp = 5.0
        private val feelsLike = 6.0
        private val humidity = 2.0
        private val pressure = 7.0

        @BeforeEach
        fun setUp() {
            val weather = mockWeather(320, description)
            val currentWeather = mockCurrentWeather(weather, temp, feelsLike, humidity, pressure)
            result = useCase(harborName, currentWeather)
        }

        @Test
        fun `drizzle harbor info is created`() {
            val expected = HarborInfo(
                harborName,
                WeatherIcon.Drizzle,
                description,
                temp.toString(),
                feelsLike.toString(),
                humidity.toString(),
                pressure.toString()
            )
            assertEquals(expected, result)
        }
    }

    @Nested
    inner class HarborInfoRain {

        private val harborName = "test 6"
        private val description = "hello 7 2"
        private val temp = 8.0
        private val feelsLike = 8.0
        private val humidity = 6.0
        private val pressure = 6.0

        @BeforeEach
        fun setUp() {
            val weather = mockWeather(520, description)
            val currentWeather = mockCurrentWeather(weather, temp, feelsLike, humidity, pressure)
            result = useCase(harborName, currentWeather)
        }

        @Test
        fun `rain harbor info is created`() {
            val expected = HarborInfo(
                harborName,
                WeatherIcon.Rain,
                description,
                temp.toString(),
                feelsLike.toString(),
                humidity.toString(),
                pressure.toString()
            )
            assertEquals(expected, result)
        }
    }

    @Nested
    inner class HarborInfoSnow {

        private val harborName = "test 6"
        private val description = "hello 7 2"
        private val temp = 8.0
        private val feelsLike = 8.0
        private val humidity = 6.0
        private val pressure = 6.0

        @BeforeEach
        fun setUp() {
            val weather = mockWeather(601, description)
            val currentWeather = mockCurrentWeather(weather, temp, feelsLike, humidity, pressure)
            result = useCase(harborName, currentWeather)
        }

        @Test
        fun `snow harbor info is created`() {
            val expected = HarborInfo(
                harborName,
                WeatherIcon.Snow,
                description,
                temp.toString(),
                feelsLike.toString(),
                humidity.toString(),
                pressure.toString()
            )
            assertEquals(expected, result)
        }
    }

    @Nested
    inner class HarborInfoAtmosphere {

        private val harborName = "test g"
        private val description = "hello f"
        private val temp = 8.0
        private val feelsLike = 9.0
        private val humidity = 8.0
        private val pressure = 8.0

        @BeforeEach
        fun setUp() {
            val weather = mockWeather(750, description)
            val currentWeather = mockCurrentWeather(weather, temp, feelsLike, humidity, pressure)
            result = useCase(harborName, currentWeather)
        }

        @Test
        fun `atmosphere harbor info is created`() {
            val expected = HarborInfo(
                harborName,
                WeatherIcon.Atmosphere,
                description,
                temp.toString(),
                feelsLike.toString(),
                humidity.toString(),
                pressure.toString()
            )
            assertEquals(expected, result)
        }
    }

    @Nested
    inner class HarborInfoClear {

        private val harborName = "test vvv"
        private val description = "hello bbbb"
        private val temp = 86.0
        private val feelsLike = 49.0
        private val humidity = 83.0
        private val pressure = 28.0

        @BeforeEach
        fun setUp() {
            val weather = mockWeather(800, description)
            val currentWeather = mockCurrentWeather(weather, temp, feelsLike, humidity, pressure)
            result = useCase(harborName, currentWeather)
        }

        @Test
        fun `clear harbor info is created`() {
            val expected = HarborInfo(
                harborName,
                WeatherIcon.Clear,
                description,
                temp.toString(),
                feelsLike.toString(),
                humidity.toString(),
                pressure.toString()
            )
            assertEquals(expected, result)
        }
    }

    @Nested
    inner class HarborInfoClouds {

        private val harborName = "test eee"
        private val description = "hello qqq"
        private val temp = 99.0
        private val feelsLike = 99.0
        private val humidity = 99.0
        private val pressure = 99.0

        @BeforeEach
        fun setUp() {
            val weather = mockWeather(804, description)
            val currentWeather = mockCurrentWeather(weather, temp, feelsLike, humidity, pressure)
            result = useCase(harborName, currentWeather)
        }

        @Test
        fun `clouds harbor info is created`() {
            val expected = HarborInfo(
                harborName,
                WeatherIcon.Clouds,
                description,
                temp.toString(),
                feelsLike.toString(),
                humidity.toString(),
                pressure.toString()
            )
            assertEquals(expected, result)
        }
    }

    @Nested
    inner class HarborInfoUnknown {

        private val harborName = "test 5"
        private val description = "hello 5"
        private val temp = 100.0
        private val feelsLike = 100.0
        private val humidity = 100.0
        private val pressure = 100.0

        @BeforeEach
        fun setUp() {
            val weather = mockWeather(1000, description)
            val currentWeather = mockCurrentWeather(weather, temp, feelsLike, humidity, pressure)
            result = useCase(harborName, currentWeather)
        }

        @Test
        fun `unknown harbor info is created`() {
            val expected = HarborInfo(
                harborName,
                WeatherIcon.Unknown,
                description,
                temp.toString(),
                feelsLike.toString(),
                humidity.toString(),
                pressure.toString()
            )
            assertEquals(expected, result)
        }
    }

    @Nested
    inner class HarborInfoWeatherNull {

        private val harborName = "test 5"
        private val temp = 100.0
        private val feelsLike = 100.0
        private val humidity = 100.0
        private val pressure = 100.0

        @BeforeEach
        fun setUp() {
            val currentWeather = mockCurrentWeather(null, temp, feelsLike, humidity, pressure)
            result = useCase(harborName, currentWeather)
        }

        @Test
        fun `unknown harbor info is created`() {
            val expected = HarborInfo(
                harborName,
                WeatherIcon.Unknown,
                "",
                temp.toString(),
                feelsLike.toString(),
                humidity.toString(),
                pressure.toString()
            )
            assertEquals(expected, result)
        }
    }

    private fun mockWeather(weatherId: Long, weatherDescription: String) = mockk<Weather> {
        every { id } returns weatherId
        every { description } returns weatherDescription
    }

    private fun mockCurrentWeather(
        weatherCurrent: Weather?,
        weatherTemp: Double,
        weatherFeelsLike: Double,
        weatherHumidity: Double,
        weatherPressure: Double
    ) = mockk<CurrentWeather> {
        every { weather } returns listOf(weatherCurrent)
        every { main } returns mockk {
            every { temp } returns weatherTemp
            every { feelsLike } returns weatherFeelsLike
            every { humidity } returns weatherHumidity
            every { pressure } returns weatherPressure
        }
    }
}
