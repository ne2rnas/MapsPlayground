package com.mapsplayground.remote.mappers

interface EntityMapper<R, E> {
    fun mapFromRemote(remote: R): E
}
