package com.mapsplayground.cache.preferences

import javax.inject.Inject

class ProvideCurrentTime @Inject constructor() {
    operator fun invoke(): Long = System.currentTimeMillis()
}
