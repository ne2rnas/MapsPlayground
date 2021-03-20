package com.mapsplayground.domain.repositories

import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.repository.result.ResultOf
import io.reactivex.rxjava3.core.Observable

interface HarborRepository {
    fun getHarbors(): Observable<ResultOf<List<Harbor>>>
}
