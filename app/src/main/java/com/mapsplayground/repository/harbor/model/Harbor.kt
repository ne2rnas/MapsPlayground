package com.mapsplayground.repository.harbor.model

data class Harbor(
    val id: String,
    val name: String,
    val image: String?,
    val lat: String,
    val lon: String,
    val isPriceHidden: Boolean,
    val isFree: Boolean,
    val canBook: Boolean,
    val cashOnlyBookings: Boolean,
    val notActivated: Boolean,
    val translations: List<HarborTranslation>,
    val wordpressLink: String?,
    val acceptBankPayments: Boolean,
    val acceptEpayPayments: Boolean,
    val acceptGoCardlessPayments: Boolean,
    val acceptBankPaymentsIban: String?,
    val bookOneDayOnly: Boolean
)
