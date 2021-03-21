package com.mapsplayground.remote

import com.mapsplayground.remote.harbor.models.HarborRemote
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Headers

private const val CONTENT_TYPE_JSON = "Content-type: application/json;charset=UTF-8"

interface HarbaApi {

    @GET("harbors/visible")
    @Headers(CONTENT_TYPE_JSON)
    fun getHarbors(): Single<List<HarborRemote>>
}
