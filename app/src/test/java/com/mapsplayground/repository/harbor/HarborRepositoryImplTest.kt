package com.mapsplayground.repository.harbor

import com.mapsplayground.cache.HarborCache
import com.mapsplayground.cache.preferences.ProvideCurrentTime
import com.mapsplayground.remote.HarborRemote
import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.repository.result.ResultOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class HarborRepositoryImplTest {

    private val harborRemote = mockk<HarborRemote>()
    private val harborCache = mockk<HarborCache>()
    private val provideCurrentTime = mockk<ProvideCurrentTime>(relaxed = true)

    private lateinit var repositoryImpl: HarborRepositoryImpl

    private val harbor1 = mockk<Harbor>()
    private val harbor2 = mockk<Harbor>()
    private val harbor3 = mockk<Harbor>()
    private val harbors = listOf(harbor1, harbor2, harbor3)

    @BeforeEach
    fun setUp() {
        repositoryImpl = HarborRepositoryImpl(harborRemote, harborCache, provideCurrentTime)
    }

    @Nested
    inner class GetHarbors {

        @Nested
        inner class Cached {

            private lateinit var observer: TestObserver<ResultOf<List<Harbor>>>

            @BeforeEach
            fun setUp() {
                every { harborCache.isCached(provideCurrentTime()) } returns Single.just(true)
                every { harborCache.getHarbors() } returns Single.just(harbors)
                observer = repositoryImpl.getHarbors().test()
            }

            @Test
            fun `fetch data from cache`() {
                verify { harborCache.getHarbors() }
            }

            @Test
            fun `observe list from cache`() {
                observer.assertResult(ResultOf.Success(harbors))
            }
        }

        @Nested
        inner class CachedError {

            private lateinit var observer: TestObserver<ResultOf<List<Harbor>>>
            private val error = Throwable()

            @BeforeEach
            fun setUp() {
                every { harborCache.isCached(provideCurrentTime()) } returns Single.just(true)
                every { harborCache.getHarbors() } returns Single.error(error)
                observer = repositoryImpl.getHarbors().test()
            }

            @Test
            fun `fetch data from cache`() {
                verify { harborCache.getHarbors() }
            }

            @Test
            fun `observe error from cache`() {
                observer.assertResult(ResultOf.Error(error))
            }
        }

        @Nested
        inner class NotCached {

            private lateinit var observer: TestObserver<ResultOf<List<Harbor>>>

            @BeforeEach
            fun setUp() {
                every { harborCache.isCached(provideCurrentTime()) } returns Single.just(false)
                every { harborCache.getHarbors() } returns Single.just(harbors)
                every { harborRemote.getHarbors() } returns Single.just(harbors)
                every { harborCache.saveHarbors(harbors) } returns Completable.complete()
                every { harborCache.setLastCacheTime(provideCurrentTime()) } returns Unit
                observer = repositoryImpl.getHarbors().test()
            }

            @Test
            fun `fetch data from remote`() {
                verify { harborRemote.getHarbors() }
            }

            @Test
            fun `save local data`() {
                verify { harborCache.saveHarbors(harbors) }
            }

            @Test
            fun `set last cache time`() {
                verify { harborCache.setLastCacheTime(provideCurrentTime()) }
            }

            @Test
            fun `fetch data from cache`() {
                verify { harborCache.getHarbors() }
            }

            @Test
            fun `observe list from cache`() {
                observer.assertResult(ResultOf.Success(harbors))
            }
        }

        @Nested
        inner class NotCachedError {

            private lateinit var observer: TestObserver<ResultOf<List<Harbor>>>
            private val error = Throwable()

            @BeforeEach
            fun setUp() {
                every { harborCache.isCached(provideCurrentTime()) } returns Single.just(false)
                every { harborCache.getHarbors() } returns Single.just(harbors)
                every { harborCache.saveHarbors(harbors) } returns Completable.complete()
                every { harborCache.setLastCacheTime(provideCurrentTime()) } returns Unit
                every { harborRemote.getHarbors() } returns Single.error(error)
                observer = repositoryImpl.getHarbors().test()
                observer = repositoryImpl.getHarbors().test()
            }

            @Test
            fun `fetch data from remote`() {
                verify { harborRemote.getHarbors() }
            }

            @Test
            fun `save local data`() {
                verify(exactly = 0) { harborCache.saveHarbors(harbors) }
            }

            @Test
            fun `set last cache time`() {
                verify(exactly = 0) { harborCache.setLastCacheTime(any()) }
            }

            @Test
            fun `fetch data from cache`() {
                verify { harborCache.getHarbors() }
            }

            @Test
            fun `observe error from remote`() {
                observer.assertResult(ResultOf.Error(error))
            }
        }
    }
}
