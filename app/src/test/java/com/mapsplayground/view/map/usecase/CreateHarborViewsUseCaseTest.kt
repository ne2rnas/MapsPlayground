package com.mapsplayground.view.map.usecase

import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.view.map.HarborView
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.osmdroid.util.GeoPoint

class CreateHarborViewsUseCaseTest {

    private lateinit var useCase: CreateHarborViewsUseCase

    @BeforeEach
    fun setUp() {
        useCase = CreateHarborViewsUseCase()
    }

    @Nested
    inner class ValidHarbor {

        private val harborLat = "50.0"
        private val harborLon = "60.0"
        private val harborName = "test"

        private val harbor = mockk<Harbor> {
            every { lat } returns harborLat
            every { lon } returns harborLon
            every { name } returns harborName
        }

        private lateinit var result: List<HarborView>

        @BeforeEach
        fun setUp() {
            result = useCase(listOf(harbor))
        }

        @Test
        fun `harbor view is created`() {
            val geoPoint = GeoPoint(harborLat.toDouble(), harborLon.toDouble())
            val harborView = HarborView(geoPoint, harborName)
            assertEquals(listOf(harborView), result)
        }
    }

    @Nested
    inner class InvalidHarbor {

        private val harborLat = "-999"
        private val harborLon = "-555"
        private val harborName = "test"

        private val harbor = mockk<Harbor> {
            every { lat } returns harborLat
            every { lon } returns harborLon
            every { name } returns harborName
        }

        private lateinit var result: List<HarborView>

        @BeforeEach
        fun setUp() {
            result = useCase(listOf(harbor))
        }

        @Test
        fun `harbor view is not created`() {
            assertEquals(emptyList<Harbor>(), result)
        }
    }
}
