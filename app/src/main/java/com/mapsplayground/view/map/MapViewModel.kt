package com.mapsplayground.view.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapsplayground.repository.harbor.HarborRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    harborRepository: HarborRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _state = MutableLiveData(ViewState())
    val state: LiveData<ViewState> get() = _state

    init {
        harborRepository.getHarbors()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    _state.value = _state.value!!.copy(isLoading = false)
                    Log.e("marius", it.toString())
                },
                {
                    _state.value = _state.value!!.copy(isLoading = false)
                    Log.e("marius", "error", it)
                }
            )
            .addTo(disposable)
    }

    data class ViewState(
        val isLoading: Boolean = true
    )

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
