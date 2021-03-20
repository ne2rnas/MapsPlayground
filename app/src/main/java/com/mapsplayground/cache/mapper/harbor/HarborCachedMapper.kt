package com.mapsplayground.cache.mapper.harbor

import com.mapsplayground.cache.mapper.CachedMapper
import com.mapsplayground.cache.models.harbor.HarborCache
import com.mapsplayground.cache.models.harbor.HarborTranslationCache
import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.repository.harbor.model.HarborTranslation
import javax.inject.Inject

class HarborCachedMapper @Inject constructor() : CachedMapper<HarborCache, Harbor> {
    override fun mapFromCached(type: HarborCache): Harbor {
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
            translations = type.translations?.map {
                HarborTranslation(
                    id = it.id,
                    name = it.name,
                    content = it.content,
                    locale = it.locale
                )
            } ?: emptyList(),
            wordpressLink = type.wordpressLink,
            acceptBankPayments = type.acceptBankPayments,
            acceptEpayPayments = type.acceptEpayPayments,
            acceptGoCardlessPayments = type.acceptGoCardlessPayments,
            acceptBankPaymentsIban = type.acceptBankPaymentsIban,
            bookOneDayOnly = type.bookOneDayOnly
        )
    }

    override fun mapToCached(type: Harbor): HarborCache {
        return HarborCache(
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
                HarborTranslationCache(
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
