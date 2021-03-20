package com.mapsplayground.repository.harbor

import com.mapsplayground.repository.harbor.model.HarborItem
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class HarborRepositoryImpl @Inject constructor(
    private val harborRemote: HarborRemote
) : HarborRepository {

    override fun getHarbors(): Single<List<HarborItem>> {
        return harborRemote.getHarbors()
    }
}
