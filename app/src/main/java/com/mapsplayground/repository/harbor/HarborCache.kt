package com.mapsplayground.repository.harbor

import com.mapsplayground.repository.harbor.model.Harbor
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface HarborCache {
    fun getHarbors(): Observable<List<Harbor>>
    fun saveHarbors(harbors: List<Harbor>): Completable
    fun clearTable(): Completable
}
