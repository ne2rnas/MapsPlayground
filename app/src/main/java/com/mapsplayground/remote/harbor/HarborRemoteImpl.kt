package com.mapsplayground.remote.harbor

import com.mapsplayground.remote.HarbaApi
import com.mapsplayground.remote.HarborRemote
import com.mapsplayground.remote.mappers.harbor.HarborEntityMapper
import com.mapsplayground.repository.harbor.model.Harbor
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class HarborRemoteImpl @Inject constructor(
    private val harbaApi: HarbaApi,
    private val harborEntityMapper: HarborEntityMapper
) : HarborRemote {

    override fun getHarbors(): Single<List<Harbor>> {
        return harbaApi.getHarbors().map { harborItemsRemote ->
            harborItemsRemote.map { harborItemRemote ->
                harborEntityMapper.mapFromRemote(harborItemRemote)
            }
        }
    }
}
