package com.mapsplayground.repository

import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.utils.result.ResultOf
import io.reactivex.rxjava3.core.Single

interface HarborRepository {
    fun getHarbors(): Single<ResultOf<List<Harbor>>>
}
