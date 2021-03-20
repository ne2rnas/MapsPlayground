package com.mapsplayground.remote.model.harbor

import com.squareup.moshi.Json

data class TranslationRemote(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "content") val content: String,
    @Json(name = "locale") val locale: String
)
