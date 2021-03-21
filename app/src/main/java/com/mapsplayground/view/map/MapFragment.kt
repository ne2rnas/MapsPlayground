package com.mapsplayground.view.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mapsplayground.databinding.FragmentMapBinding
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@AndroidEntryPoint
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMapWithDefaultValues(binding.mapView)
        viewModel.state.observe(
            viewLifecycleOwner,
            { viewState ->
                binding.progressBar.isVisible = viewState.isLoading
                viewState.harbors?.getContentIfNotHandled()?.let { harbors ->
                    with(binding.mapView) {
                        val coordinates = harbors.filter {
                            isValidCoordinate(it.lat, it.lon)
                        }.map { harbor ->
                            Marker(this).apply {
                                Log.e("latitutde:", harbor.lat)
                                Log.e("longitude:", harbor.lon)
                                position = GeoPoint(harbor.lat.toDouble(), harbor.lon.toDouble())
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                title = harbor.name
                            }
                        }
                        overlays.run {
                            clear()
                            addAll(coordinates)
                        }
                        val minLat = coordinates.minOf { it.position.latitude }
                        val maxLat = coordinates.maxOf { it.position.latitude }
                        val minLong = coordinates.minOf { it.position.longitude }
                        val maxLong = coordinates.maxOf { it.position.longitude }
                        val boundingBox = BoundingBox(maxLat, maxLong, minLat, minLong)
                        zoomToBoundingBox(boundingBox, true)
                    }
                }
            }
        )
    }

    private fun isValidCoordinate(lat: String, lon: String): Boolean {
        val maxLatCoordinateValue = MAX_NORTH_COORDINATE.toString()
        val latRegex =
            Regex("^(\\+|-)?(?:$maxLatCoordinateValue(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))\$")
        val maxLonCoordinateValue = MAX_EAST_COORDINATE.toString()
        val lonRegex =
            Regex("^(\\+|-)?(?:$maxLonCoordinateValue(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))\$")
        return latRegex.matches(lat) && lonRegex.matches(lon)
    }

    private fun initializeMapWithDefaultValues(mapView: MapView) {
        with(mapView) {
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
            //
            //            val marker = Marker(this)
            //            marker.position = center
            //            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            //            marker.title = "Koriyama-station"
            //            overlays.add(marker)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDetach() {
        super.onDetach()
        binding.mapView.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
