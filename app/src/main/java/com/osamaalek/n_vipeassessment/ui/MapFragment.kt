package com.osamaalek.n_vipeassessment.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.osamaalek.n_vipeassessment.R
import com.osamaalek.n_vipeassessment.databinding.FragmentMapBinding
import com.osamaalek.n_vipeassessment.viewmodel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private val args by navArgs<MapFragmentArgs>()
    private lateinit var googleMap: GoogleMap
    private val viewModel: MapViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMapBinding.bind(view)

        initViews(savedInstanceState)
        initObservers()
    }

    private fun initViews(savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        binding.incMapDetails.vm = viewModel
        binding.incMapDetails.origin = args.sourceAddress?.name
        binding.incMapDetails.destination = args.destinationAddress?.name
        binding.incMapDetails.lifecycleOwner = viewLifecycleOwner
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.polylineOptionsState.collect {
                    if (::googleMap.isInitialized) {
                        googleMap.addPolyline(it)
                    }
                }
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0;

        if (args.sourceAddress != null && args.destinationAddress != null) {
            val sourceLatLng = LatLng(args.sourceAddress!!.latitude, args.sourceAddress!!.longitude)
            val destinationLatLng =
                LatLng(args.destinationAddress!!.latitude, args.destinationAddress!!.longitude)

            googleMap.addMarker(
                MarkerOptions().position(sourceLatLng).title(args.sourceAddress?.name)
            )
            googleMap.addMarker(
                MarkerOptions().position(destinationLatLng).title(args.destinationAddress?.name)
            )

            viewModel.fetchDirections(args.sourceAddress!!, args.destinationAddress!!)

            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    sourceLatLng,
                    12f
                )
            )
        }
    }
}