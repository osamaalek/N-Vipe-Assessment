package com.osamaalek.n_vipeassessment.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.osamaalek.n_vipeassessment.R
import com.osamaalek.n_vipeassessment.databinding.FragmentMapBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private val args by navArgs<MapFragmentArgs>()
    private lateinit var googleMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMapBinding.bind(view)

        initViews(savedInstanceState)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
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

            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    sourceLatLng,
                    12f
                )
            )
        }
    }
}