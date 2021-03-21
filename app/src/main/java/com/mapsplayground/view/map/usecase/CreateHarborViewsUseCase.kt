package com.mapsplayground.view.map.usecase

import com.mapsplayground.repository.harbor.model.Harbor
import com.mapsplayground.view.map.HarborView
import com.mapsplayground.view.map.utils.CoordinateRegex.latRegex
import com.mapsplayground.view.map.utils.CoordinateRegex.lonRegex
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

class CreateHarborViewsUseCase @Inject constructor() {

    operator fun invoke(harbors: List<Harbor>): List<HarborView> {
        return harbors.filter {
            isValidCoordinate(it.lat, it.lon)
        }.map { harbor ->
            val geoPoint = GeoPoint(harbor.lat.toDouble(), harbor.lon.toDouble())
            HarborView(geoPoint, harbor.name)
        }
    }

    private fun isValidCoordinate(lat: String, lon: String): Boolean {
        return latRegex.matches(lat) && lonRegex.matches(lon)
    }
}
