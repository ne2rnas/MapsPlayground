package com.mapsplayground.view.map

import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import com.mapsplayground.domain.interactors.GetHarborsUseCase
import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.utils.result.ResultOf
import com.mapsplayground.utils.Event
import com.mapsplayground.utils.InstantExecutorExtension
import com.mapsplayground.utils.RxSchedulerExtension
import com.mapsplayground.utils.SimpleEvent
import com.mapsplayground.view.map.usecase.CreateHarborViewsUseCase
import com.mapsplayground.view.map.usecase.GetHarborInfoUseCase
import com.mapsplayground.view.map.usecase.GetWeatherUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.osmdroid.views.overlay.Marker

@ExtendWith(value = [RxSchedulerExtension::class, InstantExecutorExtension::class])
class MapViewModelTest {

    private val getHarborsUseCase = mockk<GetHarborsUseCase>()
    private val createHarborViewsUseCase = mockk<CreateHarborViewsUseCase>()
    private val getWeatherUseCase = mockk<GetWeatherUseCase>()
    private val getHarborInfoUseCase = mockk<GetHarborInfoUseCase>()

    private val harbor = mockk<Harbor>()
    private val harbors = listOf(harbor)

    private val harborView = mockk<HarborView>()
    private val harborViews = listOf(harborView)

    private lateinit var viewModel: MapViewModel

    @BeforeEach
    fun setUp() {
        every { createHarborViewsUseCase(harbors) } returns harborViews
    }

    @Nested
    inner class GetHarbors {

        @Nested
        inner class Success {

            @BeforeEach
            fun setUp() {
                mockDefaultHarborsUseCase()
                createViewModel()
            }

            @Test
            fun `is not loading`() {
                assertEquals(false, viewModel.state.value!!.isLoading)
            }

            @Test
            fun `harbors are created`() {
                verify { createHarborViewsUseCase(harbors) }
            }

            @Test
            fun `harbors are set`() {
                assertEquals(harborViews, viewModel.state.value!!.harbors)
            }
        }

        @Nested
        inner class Error {

            @BeforeEach
            fun setUp() {
                every { getHarborsUseCase() } returns Single.just(ResultOf.Error(Throwable()))
                createViewModel()
            }

            @Test
            fun `is not loading`() {
                assertEquals(false, viewModel.state.value!!.isLoading)
            }

            @Test
            fun `error is shown`() {
                assertThat(
                    viewModel.state.value!!.showError,
                    instanceOf(SimpleEvent::class.java)
                )
            }
        }

        @Nested
        inner class UnrecoverableError {

            @BeforeEach
            fun setUp() {
                every { getHarborsUseCase() } returns Single.error(Throwable())
                createViewModel()
            }

            @Test
            fun `is not loading`() {
                assertEquals(false, viewModel.state.value!!.isLoading)
            }

            @Test
            fun `error is shown`() {
                assertThat(
                    viewModel.state.value!!.showError,
                    instanceOf(SimpleEvent::class.java)
                )
            }
        }
    }

    @Nested
    inner class LoadWeather {

        private val markerTitle = "test"
        private val markerLatitude = 1.0
        private val markerLongitude = 2.0

        private val marker = mockk<Marker> {
            every { title } returns markerTitle
            every { position } returns mockk {
                every { latitude } returns markerLatitude
                every { longitude } returns markerLongitude
            }
        }

        @BeforeEach
        fun setUp() {
            mockDefaultHarborsUseCase()
            createViewModel()
        }

        @Nested
        inner class Success {

            private val currentWeather = mockk<CurrentWeather>()

            private val expectedResult = mockk<HarborInfo>()

            @BeforeEach
            fun setUp() {
                every { getWeatherUseCase(markerLatitude, markerLongitude) } returns
                        Single.just(ResultOf.Success(currentWeather))

                every { getHarborInfoUseCase(markerTitle, currentWeather) } returns expectedResult

                viewModel.loadWeather(marker)
            }

            @Test
            fun `is not loading`() {
                assertEquals(false, viewModel.state.value!!.isLoading)
            }

            @Test
            fun `weather is fetched`() {
                verify { getWeatherUseCase(markerLatitude, markerLongitude) }
            }

            @Test
            fun `show harbor dialog`() {
                assertThat(
                    viewModel.state.value!!.showHarborDialog,
                    instanceOf(Event::class.java)
                )
            }

            @Test
            fun `dialog contents are set`() {
                assertEquals(
                    expectedResult,
                    viewModel.state.value!!.showHarborDialog!!.peekContent()
                )
            }
        }

        @Nested
        inner class Error {

            @BeforeEach
            fun setUp() {
                every { getWeatherUseCase(markerLatitude, markerLongitude) } returns
                        Single.just(ResultOf.Error(Throwable()))

                viewModel.loadWeather(marker)
            }

            @Test
            fun `is not loading`() {
                assertEquals(false, viewModel.state.value!!.isLoading)
            }

            @Test
            fun `error is shown`() {
                assertThat(
                    viewModel.state.value!!.showError,
                    instanceOf(SimpleEvent::class.java)
                )
            }
        }

        @Nested
        inner class UnrecoverableError {

            @BeforeEach
            fun setUp() {
                every { getWeatherUseCase(markerLatitude, markerLongitude) } returns
                        Single.error(Throwable())

                viewModel.loadWeather(marker)
            }

            @Test
            fun `is not loading`() {
                assertEquals(false, viewModel.state.value!!.isLoading)
            }

            @Test
            fun `error is shown`() {
                assertThat(
                    viewModel.state.value!!.showError,
                    instanceOf(SimpleEvent::class.java)
                )
            }
        }
    }

    private fun mockDefaultHarborsUseCase() {
        every { getHarborsUseCase() } returns Single.just(ResultOf.Success(harbors))
    }

    private fun createViewModel() {
        viewModel = MapViewModel(
            getHarborsUseCase,
            createHarborViewsUseCase,
            getWeatherUseCase,
            getHarborInfoUseCase
        )
    }
}
