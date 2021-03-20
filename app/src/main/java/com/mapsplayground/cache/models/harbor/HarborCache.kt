package com.mapsplayground.cache.models.harbor

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mapsplayground.cache.db.CacheConstants

@Entity(tableName = CacheConstants.HARBOR_ITEM_TABLE_NAME)
data class HarborCache(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "image")
    val image: String?,
    @ColumnInfo(name = "lat")
    val lat: String,
    @ColumnInfo(name = "lon")
    val lon: String,
    @ColumnInfo(name = "isPriceHidden")
    val isPriceHidden: Boolean,
    @ColumnInfo(name = "isFree")
    val isFree: Boolean,
    @ColumnInfo(name = "canBook")
    val canBook: Boolean,
    @ColumnInfo(name = "cashOnlyBookings")
    val cashOnlyBookings: Boolean,
    @ColumnInfo(name = "notActivated")
    val notActivated: Boolean,
    @ColumnInfo(name = "translations")
    val translations: List<HarborTranslationCache>?,
    @ColumnInfo(name = "wordpressLink")
    val wordpressLink: String?,
    @ColumnInfo(name = "acceptBankPayments")
    val acceptBankPayments: Boolean,
    @ColumnInfo(name = "acceptEpayPayments")
    val acceptEpayPayments: Boolean,
    @ColumnInfo(name = "acceptGoCardlessPayments")
    val acceptGoCardlessPayments: Boolean,
    @ColumnInfo(name = "acceptBankPaymentsIban")
    val acceptBankPaymentsIban: String?,
    @ColumnInfo(name = "bookOneDayOnly")
    val bookOneDayOnly: Boolean
)
