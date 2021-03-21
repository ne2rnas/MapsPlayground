package com.mapsplayground.cache.harbor

import com.mapsplayground.cache.HarborCache
import com.mapsplayground.cache.db.MapsPlaygroundDatabase
import com.mapsplayground.cache.mapper.harbor.HarborCachedMapper
import com.mapsplayground.cache.preferences.CachePrefs
import com.mapsplayground.repository.harbor.model.Harbor
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

const val CACHE_EXPIRATION_TIME = (60 * 60 * 1000).toLong() // 1 hour

class HarborCacheImpl @Inject constructor(
    private val database: MapsPlaygroundDatabase,
    private val harborCachedMapper: HarborCachedMapper,
    private val cachePrefs: CachePrefs
) : HarborCache {

    override fun getHarbors(): Single<List<Harbor>> {
        return Single.defer {
            database.harborDao().getHarbors().map { harborCaches ->
                harborCaches.map { harborCache ->
                    harborCachedMapper.mapFromCached(harborCache)
                }
            }
        }
    }

    override fun saveHarbors(harbors: List<Harbor>): Completable {
        return Completable.defer {
            harbors.map { harborCachedMapper.mapToCached(it) }.also { harborCaches ->
                with(database) {
                    runInTransaction {
                        harborDao().run {
                            clearTable()
                            saveHarbors(harborCaches)
                        }
                    }
                }
            }
            Completable.complete()
        }
    }

    override fun isCached(currentTime: Long): Single<Boolean> {
        return Single.defer {
            database.harborDao().loadOneHarbor().map { harbors ->
                !isExpired(currentTime) && harbors.isNotEmpty()
            }
        }
    }

    override fun setLastCacheTime(currentTime: Long) {
        cachePrefs.harborsCacheTime = currentTime
    }

    private fun isExpired(currentTime: Long): Boolean {
        val lastUpdateTime = cachePrefs.harborsCacheTime
        return currentTime - lastUpdateTime > CACHE_EXPIRATION_TIME
    }
}
