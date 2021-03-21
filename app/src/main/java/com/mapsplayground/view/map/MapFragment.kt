package com.mapsplayground.view.map

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.mapsplayground.R
import com.mapsplayground.databinding.FragmentMapBinding
import com.mapsplayground.utils.whenNotNullNorEmpty
import com.mapsplayground.view.map.utils.addMarkers
import com.mapsplayground.view.map.utils.createMarker
import com.mapsplayground.view.map.utils.initializeMapWithDefaultValues
import com.mapsplayground.view.map.utils.zoomToBoundingBox
import dagger.hilt.android.AndroidEntryPoint

private const val DIALOG_CORNER_RADIUS = 16F

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
        viewState.showHarborDialog?.getContentIfNotHandled()?.let { harborInfo ->
            showHarborInfoDialog(harborInfo)
        }
        viewState.showError?.getContentIfNotHandled()?.let {
            showError()
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

    private fun showHarborInfoDialog(harborInfo: HarborInfo) {
        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(text = harborInfo.harborName)
            icon(getHarborInfoDialogIcon(harborInfo.weatherIcon))
            customView(R.layout.dialog_harbor_info, horizontalPadding = true)
            cornerRadius(DIALOG_CORNER_RADIUS)
            negativeButton(R.string.close)
            lifecycleOwner(viewLifecycleOwner)
        }.also {
            it.getCustomView().run {
                findViewById<TextView>(R.id.harbor_info_description).apply {
                    text = harborInfo.description ?: ""
                }
                findViewById<TextView>(R.id.harbor_info_temperature).apply {
                    text = String.format(
                        getString(R.string.temperature),
                        harborInfo.temp
                    )
                }
                findViewById<TextView>(R.id.harbor_info_feels_like).apply {
                    text = String.format(
                        getString(R.string.temperature),
                        harborInfo.feelsLike
                    )
                }
                findViewById<TextView>(R.id.harbor_info_humidity).apply {
                    text = String.format(
                        getString(R.string.percentage),
                        harborInfo.humidity
                    )
                }
                findViewById<TextView>(R.id.harbor_info_pressure).apply {
                    text = String.format(
                        getString(R.string.pressure),
                        harborInfo.pressure
                    )
                }
            }
        }
    }

    private fun getHarborInfoDialogIcon(weatherIcon: WeatherIcon): Int {
        return when (weatherIcon) {
            is WeatherIcon.Thunderstorm -> R.drawable.weather_11d
            is WeatherIcon.Drizzle -> R.drawable.weather_09d
            is WeatherIcon.Rain -> R.drawable.weather_10d
            is WeatherIcon.Snow -> R.drawable.weather_13d
            is WeatherIcon.Atmosphere -> R.drawable.weather_50d
            is WeatherIcon.Clear -> R.drawable.weather_01d
            is WeatherIcon.Clouds -> R.drawable.weather_02d
            is WeatherIcon.Unknown -> R.drawable.weather_50d
        }
    }

    private fun showError() {
        Snackbar.make(binding.root, resources.getString(R.string.error), Snackbar.LENGTH_LONG)
            .show()
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
