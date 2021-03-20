package com.mapsplayground.repository.harbor

import com.mapsplayground.repository.harbor.model.Harbor
import io.reactivex.rxjava3.core.Single

interface HarborRemote {
    fun getHarbors(): Single<List<Harbor>>
}
