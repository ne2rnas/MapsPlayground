package com.mapsplayground.cache.harbor

import com.mapsplayground.cache.db.MapsPlaygroundDatabase
import com.mapsplayground.cache.harbor.models.HarborCache
import com.mapsplayground.cache.mapper.harbor.HarborCachedMapper
import com.mapsplayground.cache.preferences.CachePrefs
import com.mapsplayground.repository.harbor.model.Harbor
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class HarborCacheImplTest {

    private val database = mockk<MapsPlaygroundDatabase>()
    private val harborCachedMapper = mockk<HarborCachedMapper>()
    private val cachePrefs = mockk<CachePrefs>()

    private lateinit var harborCacheImpl: HarborCacheImpl

    private val harbor = mockk<Harbor>()
    private val harbors = listOf(harbor)
    private val harborCached = mockk<HarborCache>()
    private val harborsCached = listOf(harborCached)

    @BeforeEach
    fun setUp() {
        every { database.runInTransaction(any()) } answers {
            firstArg<Runnable>().run()
        }
        harborCacheImpl = HarborCacheImpl(database, harborCachedMapper, cachePrefs)
    }

    @Nested
    inner class GetHarbors {

        private lateinit var observer: TestObserver<List<Harbor>>

        @BeforeEach
        fun setUp() {
            every { database.harborDao().getHarbors() } returns Single.just(harborsCached)
            every { harborCachedMapper.mapFromCached(harborCached) } returns harbor
            observer = harborCacheImpl.getHarbors().test()
        }

        @Test
        fun `get harbors from database`() {
            verify { database.harborDao().getHarbors() }
        }

        @Test
        fun `map harbor from cached`() {
            verify { harborCachedMapper.mapFromCached(harborCached) }
        }

        @Test
        fun `observe mapped list`() {
            observer.assertResult(harbors)
        }
    }

    @Nested
    inner class SaveHarbors {

        private lateinit var observer: TestObserver<Void>

        @BeforeEach
        fun setUp() {
            every { harborCachedMapper.mapToCached(harbor) } returns harborCached
            every { database.harborDao().clearTable() } returns Unit
            every { database.harborDao().saveHarbors(harborsCached) } returns Unit
            observer = harborCacheImpl.saveHarbors(harbors).test()
        }

        @Test
        fun `map harbor to cached`() {
            verify { harborCachedMapper.mapToCached(harbor) }
        }

        @Test
        fun `clear table`() {
            verify { database.harborDao().clearTable() }
        }

        @Test
        fun `save harbors to database`() {
            verify { database.harborDao().saveHarbors(harborsCached) }
        }

        @Test
        fun `observe operation completed`() {
            observer.assertComplete()
        }
    }

    @Nested
    inner class IsCached {

        private lateinit var observer: TestObserver<Boolean>

        @BeforeEach
        fun setUp() {
            every { database.harborDao().loadOneHarbor() } returns Single.just(harborsCached)
            every { cachePrefs.harborsCacheTime } returns TimeUnit.HOURS.toMillis(1)
            val currentTime = TimeUnit.HOURS.toMillis(1)
            observer = harborCacheImpl.isCached(currentTime).test()
        }

        @Test
        fun `one harbor loaded from cache`() {
            verify { database.harborDao().loadOneHarbor() }
        }

        @Test
        fun `is cached`() {
            observer.assertResult(true)
        }
    }

    @Nested
    inner class IsNotCached {

        private lateinit var observer: TestObserver<Boolean>

        @Nested
        inner class TimerExpired {

            @BeforeEach
            fun setUp() {
                every { database.harborDao().loadOneHarbor() } returns Single.just(harborsCached)
                every { cachePrefs.harborsCacheTime } returns TimeUnit.HOURS.toMillis(1)
                val currentTime = TimeUnit.HOURS.toMillis(2) + TimeUnit.MINUTES.toMillis(1)
                observer = harborCacheImpl.isCached(currentTime).test()
            }

            @Test
            fun `one harbor loaded from cache`() {
                verify { database.harborDao().loadOneHarbor() }
            }

            @Test
            fun `is not cached`() {
                observer.assertResult(false)
            }
        }

        @Nested
        inner class CacheIsEmpty {

            @BeforeEach
            fun setUp() {
                every { database.harborDao().loadOneHarbor() } returns Single.just(emptyList())
                every { cachePrefs.harborsCacheTime } returns TimeUnit.HOURS.toMillis(1)
                val currentTime = TimeUnit.HOURS.toMillis(1)
                observer = harborCacheImpl.isCached(currentTime).test()
            }

            @Test
            fun `one harbor loaded from cache`() {
                verify { database.harborDao().loadOneHarbor() }
            }

            @Test
            fun `is not cached`() {
                observer.assertResult(false)
            }
        }
    }

    @Nested
    inner class SetLastCacheTime {

        private val slot = slot<Long>()
        private val time = 1L

        @BeforeEach
        fun setUp() {
            every { cachePrefs.harborsCacheTime = capture(slot) } answers { }
            harborCacheImpl.setLastCacheTime(time)
        }

        @Test
        fun `set harbors cache time`() {
            assertEquals(time, slot.captured)
        }
    }
}
