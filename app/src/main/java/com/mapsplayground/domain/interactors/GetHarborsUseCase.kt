package com.mapsplayground.domain.interactors

import com.mapsplayground.repository.HarborRepository
import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.utils.result.ResultOf
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetHarborsUseCase @Inject constructor(private val harborRepository: HarborRepository) {
    operator fun invoke(): Single<ResultOf<List<Harbor>>> = harborRepository.getHarbors()
}
