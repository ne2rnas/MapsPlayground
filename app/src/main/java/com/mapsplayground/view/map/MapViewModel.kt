package com.mapsplayground.view.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapsplayground.domain.interactors.GetHarborsUseCase
import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.repository.result.doIfError
import com.mapsplayground.repository.result.doIfSuccess
import com.mapsplayground.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    getHarborsUseCase: GetHarborsUseCase
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
                        Log.e("marius", harbors.toString())
                        _state.value = _state.value!!.copy(
                            isLoading = false,
                            harbors = Event(harbors)
                        )
                    }
                    result.doIfError {
                        _state.value = _state.value!!.copy(isLoading = false)
                        Log.e("marius", "error", it)
                    }

                },
                {
                    _state.value = _state.value!!.copy(isLoading = false)
                    Log.e("marius", "error", it)
                }
            )
            .addTo(disposable)
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val harbors: Event<List<Harbor>>? = null
    )

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
