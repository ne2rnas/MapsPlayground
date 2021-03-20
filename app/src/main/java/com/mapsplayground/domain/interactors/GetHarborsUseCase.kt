package com.mapsplayground.domain.interactors

import com.mapsplayground.domain.repositories.HarborRepository
import com.mapsplayground.repository.harbor.model.HarborItem
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetHarborsUseCase @Inject constructor(private val harborRepository: HarborRepository) {
    operator fun invoke(): Single<List<HarborItem>> = harborRepository.getHarbors()
}
