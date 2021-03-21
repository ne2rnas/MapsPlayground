package com.mapsplayground.view.map

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mapsplayground.R
import com.mapsplayground.databinding.FragmentMapBinding
import com.mapsplayground.utils.whenNotNullNorEmpty
import com.mapsplayground.view.map.utils.addMarkers
import com.mapsplayground.view.map.utils.createMarker
import com.mapsplayground.view.map.utils.initializeMapWithDefaultValues
import com.mapsplayground.view.map.utils.zoomToBoundingBox
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    private val binding: FragmentMapBinding by lazy { FragmentMapBinding.bind(requireView()) }

    private val viewModel: MapViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.initializeMapWithDefaultValues()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.state.observe(
            viewLifecycleOwner,
            { viewState ->
                bindState(viewState)
            }
        )
    }

    private fun bindState(viewState: MapViewModel.ViewState) {
        binding.progressBar.isVisible = viewState.isLoading
        viewState.harbors.whenNotNullNorEmpty { mapViews ->
            bindMapView(mapViews)
        }
    }

    private fun bindMapView(mapViews: List<HarborView>) {
        with(binding.mapView) {
            val markers = mapViews.map { harborView ->
                createMarker(harborView) { marker ->
                    viewModel.loadWeather(marker)
                }
            }
            addMarkers(markers)
            zoomToBoundingBox(markers)
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
}
