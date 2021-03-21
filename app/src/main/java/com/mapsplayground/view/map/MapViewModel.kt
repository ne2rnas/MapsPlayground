package com.mapsplayground.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import com.mapsplayground.domain.interactors.GetHarborsUseCase
import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.repository.result.doIfError
import com.mapsplayground.repository.result.doIfSuccess
import com.mapsplayground.utils.Event
import com.mapsplayground.utils.SimpleEvent
import com.mapsplayground.view.map.usecase.CreateHarborViewsUseCase
import com.mapsplayground.view.map.usecase.GetHarborInfoUseCase
import com.mapsplayground.view.map.usecase.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import org.osmdroid.views.overlay.Marker
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    getHarborsUseCase: GetHarborsUseCase,
    private val createHarborViewsUseCase: CreateHarborViewsUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val getHarborInfoUseCase: GetHarborInfoUseCase
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _state = MutableLiveData(ViewState())
    val state: LiveData<ViewState> get() = _state

    init {
        getHarborsUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    result.doIfSuccess { harbors ->
                        onReduceState(Action.HarborsLoaded(harbors))
                    }
                    result.doIfError {
                        onReduceState(Action.HarborsError(it))
                    }

                },
                {
                    onReduceState(Action.HarborsError(it))
                }
            )
            .addTo(disposable)
    }

    fun loadWeather(marker: Marker) {
        getWeatherUseCase(marker.position.latitude, marker.position.longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                onReduceState(Action.StartLoading)
            }
            .subscribe(
                { result ->
                    result.doIfSuccess { currentWeather ->
                        onReduceState(Action.WeatherLoaded(marker.title, currentWeather))
                    }
                    result.doIfError {
                        onReduceState(Action.WeatherError(it))
                    }

                },
                {
                    onReduceState(Action.WeatherError(it))
                }
            )
            .addTo(disposable)
    }

    private fun onReduceState(action: Action) = when (action) {
        is Action.HarborsLoaded -> {
            _state.value = _state.value!!.copy(
                isLoading = false,
                harbors = createHarborViewsUseCase(action.harbors)
            )
        }
        is Action.HarborsError -> {
            _state.value = _state.value!!.copy(
                isLoading = false,
                showError = SimpleEvent()
            )
        }
        is Action.WeatherError -> {
            _state.value = _state.value!!.copy(
                isLoading = false,
                showError = SimpleEvent()
            )
        }
        is Action.WeatherLoaded -> {
            _state.value = _state.value!!.copy(
                isLoading = false,
                showHarborDialog = Event(
                    getHarborInfoUseCase(
                        action.harborName,
                        action.currentWeather
                    )
                )
            )
        }
        Action.StartLoading -> {
            _state.value = _state.value!!.copy(isLoading = true)
        }
    }

    sealed class Action {
        data class HarborsLoaded(val harbors: List<Harbor>) : Action()
        data class HarborsError(val error: Throwable?) : Action()
        data class WeatherLoaded(
            val harborName: String,
            val currentWeather: CurrentWeather
        ) : Action()

        data class WeatherError(val error: Throwable?) : Action()
        object StartLoading : Action()
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val harbors: List<HarborView> = emptyList(),
        val showHarborDialog: Event<HarborInfo>? = null,
        val showError: SimpleEvent? = null
    )

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
