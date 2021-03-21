package com.mapsplayground.view.map.utils

import com.mapsplayground.view.map.HarborView
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

fun MapView.initializeMapWithDefaultValues() {
    setMultiTouchControls(true)
    isHorizontalMapRepetitionEnabled = false
    isVerticalMapRepetitionEnabled = false
    setScrollableAreaLimitDouble(
        BoundingBox(
            MAX_NORTH_COORDINATE,
            MAX_EAST_COORDINATE,
            MAX_SOUTH_COORDINATE,
            MAX_WEST_COORDINATE
        )
    )
    minZoomLevel = MIN_ZOOM_LEVEL
    maxZoomLevel = MAX_ZOOM_LEVEL

    controller.run {
        setZoom(INITIAL_ZOOM_LEVEL)
        val centerOfTheUniverse = GeoPoint(INITIAL_POSITION_LAT, INITIAL_POSITION_LONG)
        setCenter(centerOfTheUniverse)
    }
}

fun MapView.createMarker(harborView: HarborView, callback: (value: Marker) -> Unit): Marker {
    return Marker(this).apply {
        position = GeoPoint(
            harborView.geoPoint.latitude,
            harborView.geoPoint.longitude
        )
        title = harborView.name
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        setOnMarkerClickListener { marker, _ ->
            callback(marker)
            true
        }
    }
}

fun MapView.addMarkers(markers: List<Marker>) {
    overlays.run {
        clear()
        addAll(markers)
    }
}

fun MapView.zoomToBoundingBox(markers: List<Marker>) {
    val minLat = markers.minOf { it.position.latitude }
    val maxLat = markers.maxOf { it.position.latitude }
    val minLong = markers.minOf { it.position.longitude }
    val maxLong = markers.maxOf { it.position.longitude }
    val boundingBox = BoundingBox(maxLat, maxLong, minLat, minLong)
    zoomToBoundingBox(boundingBox, true)
}
