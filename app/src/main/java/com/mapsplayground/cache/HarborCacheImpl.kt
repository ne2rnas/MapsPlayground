package com.mapsplayground.cache

import com.mapsplayground.cache.db.MapsPlaygroundDatabase
import com.mapsplayground.cache.mapper.harbor.HarborCachedMapper
import com.mapsplayground.repository.harbor.HarborCache
import com.mapsplayground.repository.harbor.model.Harbor
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class HarborCacheImpl @Inject constructor(
    private val database: MapsPlaygroundDatabase,
    private val harborCachedMapper: HarborCachedMapper
) : HarborCache {

    override fun getHarbors(): Observable<List<Harbor>> {
        return Observable.defer {
            database.harborDao().getHarbors().map { harborCaches ->
                harborCaches.map { harborCache ->
                    harborCachedMapper.mapFromCached(harborCache)
                }
            }
        }
    }

    override fun saveHarbors(harbors: List<Harbor>): Completable {
        return Completable.defer {
            harbors.map { harborCachedMapper.mapToCached(it) }.also {
                database.harborDao().saveHarbors(it)
            }
            Completable.complete()
        }
    }

    override fun clearTable(): Completable {
        return Completable.defer {
            database.harborDao().clearTable()
            Completable.complete()
        }
    }
}
