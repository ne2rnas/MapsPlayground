package com.mapsplayground.utils

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class RxSchedulerExtension(
    private val scheduler: Scheduler = Schedulers.trampoline()
) : AfterEachCallback, BeforeEachCallback {
    @Throws(Exception::class)
    override fun beforeEach(context: ExtensionContext) {
        RxAndroidPlugins.reset()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler { scheduler }
        RxJavaPlugins.setNewThreadSchedulerHandler { scheduler }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler }
    }

    @Throws(Exception::class)
    override fun afterEach(context: ExtensionContext) {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }
}
