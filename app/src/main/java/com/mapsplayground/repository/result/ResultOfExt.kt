package com.mapsplayground.repository.result

inline fun <reified T> ResultOf<T>.doIfError(callback: (throwable: Throwable?) -> Unit) {
    if (this is ResultOf.Error) {
        callback(throwable)
    }
}

inline fun <reified T> ResultOf<T>.doIfSuccess(callback: (value: T) -> Unit) {
    if (this is ResultOf.Success) {
        callback(value)
    }
}
