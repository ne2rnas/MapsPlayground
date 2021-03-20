package com.mapsplayground.cache.db

import androidx.room.TypeConverter
import com.mapsplayground.cache.models.harbor.HarborTranslationCache
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val translationsCacheAdapter =
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @TypeConverter
    fun toHarborTranslationCache(value: String?): List<HarborTranslationCache>? {
        val type = Types.newParameterizedType(List::class.java, HarborTranslationCache::class.java)
        val adapter = translationsCacheAdapter.adapter<List<HarborTranslationCache>>(type)
        return adapter.fromJson(value ?: "")
    }

    @TypeConverter
    fun fromHarborTranslationCache(value: List<HarborTranslationCache>): String {
        val type = Types.newParameterizedType(List::class.java, HarborTranslationCache::class.java)
        val adapter = translationsCacheAdapter.adapter<List<HarborTranslationCache>>(type)
        return adapter.toJson(value)
    }
}
