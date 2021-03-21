package com.mapsplayground.view.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapsplayground.domain.interactors.GetHarborsUseCase
import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.repository.result.doIfError
import com.mapsplayground.repository.result.doIfSuccess
import com.mapsplayground.view.map.usecase.CreateHarborViewsUseCase
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
    private val createHarborViewsUseCase: CreateHarborViewsUseCase
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
                        onReduceState(Action.HarborsLoadedError(it))
                    }

                },
                {
                    onReduceState(Action.HarborsLoadedError(it))
                }
            )
            .addTo(disposable)
    }

    fun loadWeather(marker: Marker) {
        Log.e("mariusmarius", "marker clicked")
    }

    private fun onReduceState(action: Action) = when (action) {
        is Action.HarborsLoaded -> {
            _state.value = _state.value!!.copy(
                isLoading = false,
                harbors = createHarborViewsUseCase(action.harbors)
            )
        }
        is Action.HarborsLoadedError -> {
            Log.e("mariusmarius", "error", action.error)
            _state.value = _state.value!!.copy(
                isLoading = false
            )
        }
    }

    sealed class Action {
        data class HarborsLoaded(val harbors: List<Harbor>) : Action()
        data class HarborsLoadedError(val error: Throwable?) : Action()
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val harbors: List<HarborView> = emptyList()
    )

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
