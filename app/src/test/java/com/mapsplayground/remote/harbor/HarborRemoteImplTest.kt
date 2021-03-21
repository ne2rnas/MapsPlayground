package com.mapsplayground.remote.harbor

import com.mapsplayground.remote.HarbaApi
import com.mapsplayground.remote.harbor.models.HarborRemote
import com.mapsplayground.remote.mappers.harbor.HarborEntityMapper
import com.mapsplayground.repository.harbor.model.Harbor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HarborRemoteImplTest {

    private val harbaApi = mockk<HarbaApi>()
    private val harborEntityMapper = mockk<HarborEntityMapper>()

    private lateinit var harborRemoteImpl: HarborRemoteImpl

    private val harbor = mockk<Harbor>()
    private val harborRemote = mockk<HarborRemote>()
    private val harborRemoteList = listOf(harborRemote)

    private lateinit var observer: TestObserver<List<Harbor>>

    @BeforeEach
    fun setUp() {
        harborRemoteImpl = HarborRemoteImpl(harbaApi, harborEntityMapper)
        every { harbaApi.getHarbors() } returns Single.just(harborRemoteList)
        every { harborEntityMapper.mapFromRemote(harborRemote) } returns harbor
        observer = harborRemoteImpl.getHarbors().test()
    }

    @Test
    fun `fetch data from remote`() {
        verify { harbaApi.getHarbors() }
    }

    @Test
    fun `map harbor from remote`() {
        verify { harborEntityMapper.mapFromRemote(harborRemote) }
    }

    @Test
    fun `observe mapped list`() {
        observer.assertResult(listOf(harbor))
    }
}
