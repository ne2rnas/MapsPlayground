package com.mapsplayground.cache

import com.mapsplayground.repository.harbor.model.Harbor
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface HarborCache {
    fun getHarbors(): Single<List<Harbor>>
    fun saveHarbors(harbors: List<Harbor>): Completable
    fun isCached(currentTime: Long): Single<Boolean>
    fun setLastCacheTime(currentTime: Long)
}
