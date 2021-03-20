package com.mapsplayground.remote.mappers.harbor

import com.mapsplayground.remote.mappers.EntityMapper
import com.mapsplayground.remote.models.harbor.HarborRemote
import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.repository.harbor.model.HarborTranslation
import javax.inject.Inject

class HarborEntityMapper @Inject constructor() : EntityMapper<HarborRemote, Harbor> {

    override fun mapFromRemote(type: HarborRemote): Harbor {
        return Harbor(
            id = type.id,
            name = type.name,
            image = type.image,
            lat = type.lat,
            lon = type.lon,
            isPriceHidden = type.isPriceHidden,
            isFree = type.isFree,
            canBook = type.canBook,
            cashOnlyBookings = type.cashOnlyBookings,
            notActivated = type.notActivated,
            translations = type.translations.map {
                HarborTranslation(
                    id = it.id,
                    name = it.name,
                    content = it.content,
                    locale = it.locale
                )
            },
            wordpressLink = type.wordpressLink,
            acceptBankPayments = type.acceptBankPayments,
            acceptEpayPayments = type.acceptEpayPayments,
            acceptGoCardlessPayments = type.acceptGoCardlessPayments,
            acceptBankPaymentsIban = type.acceptBankPaymentsIban,
            bookOneDayOnly = type.bookOneDayOnly
        )
    }
}
