package com.mapsplayground.remote.harbor.models

import com.squareup.moshi.Json

data class HarborRemote(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "image") val image: String?,
    @Json(name = "lat") val lat: String,
    @Json(name = "lon") val lon: String,
    @Json(name = "isPriceHidden") val isPriceHidden: Boolean,
    @Json(name = "isFree") val isFree: Boolean,
    @Json(name = "canBook") val canBook: Boolean,
    @Json(name = "cashOnlyBookings") val cashOnlyBookings: Boolean,
    @Json(name = "notActivated") val notActivated: Boolean,
    @Json(name = "translations") val translations: List<HarborTranslationRemote>,
    @Json(name = "wordpressLink") val wordpressLink: String?,
    @Json(name = "acceptBankPayments") val acceptBankPayments: Boolean,
    @Json(name = "acceptEpayPayments") val acceptEpayPayments: Boolean,
    @Json(name = "acceptGoCardlessPayments") val acceptGoCardlessPayments: Boolean,
    @Json(name = "acceptBankPaymentsIban") val acceptBankPaymentsIban: String?,
    @Json(name = "bookOneDayOnly") val bookOneDayOnly: Boolean
)
