package com.mapsplayground.remote

import com.mapsplayground.remote.mappers.harbor.HarborEntityMapper
import com.mapsplayground.repository.harbor.HarborRemote
import com.mapsplayground.repository.harbor.model.HarborItem
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class HarborRemoteImpl @Inject constructor(
    private val harbaApi: HarbaApi,
    private val harborEntityMapper: HarborEntityMapper
) : HarborRemote {

    override fun getHarbors(): Single<List<HarborItem>> {
        return harbaApi.getHarbors().map { harborItemRemotes ->
            harborItemRemotes.map { harborItemRemote ->
                harborEntityMapper.mapFromRemote(harborItemRemote)
            }
        }
    }
}
