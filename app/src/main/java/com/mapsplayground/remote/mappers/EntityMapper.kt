package com.mapsplayground.remote.mappers

interface EntityMapper<R, E> {
    fun mapFromRemote(type: R): E
}
