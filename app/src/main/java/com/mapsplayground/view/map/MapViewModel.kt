package com.mapsplayground.view.map

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mapsplayground.remote.HarbaApi
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    harbaApi: HarbaApi
) : ViewModel() {

    private val disposable = CompositeDisposable()

    init {
        harbaApi.getHarbors()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.e("marius", it)
                },
                {
                    Log.e("marius", "error", it)
                }
            )
            .addTo(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
