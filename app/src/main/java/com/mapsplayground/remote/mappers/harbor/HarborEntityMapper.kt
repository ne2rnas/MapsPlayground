package com.mapsplayground.remote.mappers.harbor

import com.mapsplayground.remote.mappers.EntityMapper
import com.mapsplayground.remote.model.harbor.HarborItemRemote
import com.mapsplayground.repository.harbor.model.HarborItem
import com.mapsplayground.repository.harbor.model.Translation
import javax.inject.Inject

class HarborEntityMapper @Inject constructor() : EntityMapper<HarborItemRemote, HarborItem> {

    override fun mapFromRemote(remote: HarborItemRemote): HarborItem {
        return HarborItem(
            id = remote.id,
            name = remote.name,
            image = remote.image,
            lat = remote.lat,
            lon = remote.lon,
            isPriceHidden = remote.isPriceHidden,
            isFree = remote.isFree,
            canBook = remote.canBook,
            cashOnlyBookings = remote.cashOnlyBookings,
            notActivated = remote.notActivated,
            translationsRemote = remote.translationsRemote.map {
                Translation(
                    id = it.id,
                    name = it.name,
                    content = it.content,
                    locale = it.locale
                )
            },
            wordpressLink = remote.wordpressLink,
            acceptBankPayments = remote.acceptBankPayments,
            acceptEpayPayments = remote.acceptEpayPayments,
            acceptGoCardlessPayments = remote.acceptGoCardlessPayments,
            acceptBankPaymentsIban = remote.acceptBankPaymentsIban,
            bookOneDayOnly = remote.bookOneDayOnly
        )
    }
}
