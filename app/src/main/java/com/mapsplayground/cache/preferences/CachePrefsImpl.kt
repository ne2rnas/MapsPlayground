package com.mapsplayground.cache.preferences

import com.chibatching.kotpref.KotprefModel
import javax.inject.Inject

class CachePrefsImpl @Inject constructor() : CachePrefs, KotprefModel() {
    override var harborsCacheTime: Long by longPref(0L)
}
