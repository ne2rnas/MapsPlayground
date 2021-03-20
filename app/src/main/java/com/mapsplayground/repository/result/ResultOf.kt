package com.mapsplayground.repository.result

sealed class ResultOf<out R> {
    data class Success<out R>(val value: R) : ResultOf<R>()
    data class Error(val throwable: Throwable?) : ResultOf<Nothing>()
}
