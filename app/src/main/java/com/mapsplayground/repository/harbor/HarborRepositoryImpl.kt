package com.mapsplayground.repository.harbor

import com.mapsplayground.domain.repositories.HarborRepository
import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.repository.result.ResultOf
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class HarborRepositoryImpl @Inject constructor(
    private val harborRemote: HarborRemote,
    private val harborCache: HarborCache
) : HarborRepository {

    override fun getHarbors(): Observable<ResultOf<List<Harbor>>> {
        val cache = observeCachedData()
            .map<ResultOf<List<Harbor>>> {
                ResultOf.Success(it)
            }.onErrorReturn {
                ResultOf.Error(it)
            }
        val remote = fetchFromRemoteAndSave()
            .toObservable<ResultOf<List<Harbor>>>()
            .onErrorReturn {
                ResultOf.Error(it)
            }
        return cache.mergeWith(remote)
    }

    private fun observeCachedData(): Observable<List<Harbor>> {
        return harborCache.getHarbors()
    }

    private fun fetchFromRemoteAndSave(): Completable {
        return getRemoteData().flatMapCompletable {
            harborCache.saveHarbors(it)
        }
    }

    private fun getRemoteData(): Observable<List<Harbor>> {
        return harborRemote.getHarbors().toObservable()
    }
}
