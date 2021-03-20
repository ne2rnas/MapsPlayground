package com.mapsplayground.domain.interactors

import com.mapsplayground.domain.repositories.HarborRepository
import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.repository.result.ResultOf
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetHarborsUseCase @Inject constructor(private val harborRepository: HarborRepository) {
    operator fun invoke(): Observable<ResultOf<List<Harbor>>> = harborRepository.getHarbors()
}
