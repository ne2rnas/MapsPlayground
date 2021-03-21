package com.mapsplayground.remote.harbor.models

import com.squareup.moshi.Json

data class HarborTranslationRemote(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "content") val content: String,
    @Json(name = "locale") val locale: String
)
