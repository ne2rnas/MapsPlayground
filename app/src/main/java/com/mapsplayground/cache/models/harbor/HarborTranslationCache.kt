package com.mapsplayground.cache.models.harbor

import androidx.room.ColumnInfo

data class HarborTranslationCache(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "locale")
    val locale: String
)
