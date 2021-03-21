package com.mapsplayground.repository.harbor

import com.mapsplayground.cache.HarborCache
import com.mapsplayground.cache.preferences.ProvideCurrentTime
import com.mapsplayground.remote.HarborRemote
import com.mapsplayground.repository.HarborRepository
import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.utils.result.ResultOf
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class HarborRepositoryImpl @Inject constructor(
    private val harborRemote: HarborRemote,
    private val harborCache: HarborCache,
    private val provideCurrentTime: ProvideCurrentTime
) : HarborRepository {

    override fun getHarbors(): Single<ResultOf<List<Harbor>>> {
        return isCached().flatMap { isCached ->
            if (isCached) {
                getCachedData()
            } else {
                fetchFromRemoteAndSave().andThen(getCachedData())
            }
        }.map<ResultOf<List<Harbor>>> {
            ResultOf.Success(it)
        }.onErrorReturn {
            ResultOf.Error(it)
        }
    }

    private fun isCached(): Single<Boolean> = harborCache.isCached(provideCurrentTime())

    private fun getCachedData(): Single<List<Harbor>> = harborCache.getHarbors()

    private fun fetchFromRemoteAndSave(): Completable {
        return getRemoteData().flatMapCompletable { harbors ->
            saveLocalData(harbors)
        }
    }

    private fun getRemoteData(): Single<List<Harbor>> = harborRemote.getHarbors()

    private fun saveLocalData(harbors: List<Harbor>): Completable {
        return harborCache.saveHarbors(harbors)
            .doOnComplete {
                harborCache.setLastCacheTime(provideCurrentTime())
            }
    }
}
