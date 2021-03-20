package com.mapsplayground.cache.mapper

interface CachedMapper<T, V> {
    fun mapFromCached(type: T): V
    fun mapToCached(type: V): T
}
