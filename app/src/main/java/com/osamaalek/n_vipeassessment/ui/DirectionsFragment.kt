package com.osamaalek.n_vipeassessment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.AbstractListDetailFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.osamaalek.n_vipeassessment.BuildConfig
import com.osamaalek.n_vipeassessment.R
import com.osamaalek.n_vipeassessment.databinding.FragmentDirectionsBinding
import com.osamaalek.n_vipeassessment.model.Address

class DirectionsFragment : AbstractListDetailFragment() {
    private lateinit var originAutocompleteFragment: AutocompleteSupportFragment
    private lateinit var destinationAutocompleteFragment: AutocompleteSupportFragment
    private lateinit var binding: FragmentDirectionsBinding
    private lateinit var origin: Address
    private lateinit var destination: Address

    override fun onCreateListPaneView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDirectionsBinding.inflate(inflater, container, false)

        init()
        initViews()
        initListeners()

        return binding.root
    }

    private fun init() {
        if (!Places.isInitialized()) {

            Places.initialize(requireContext(), BuildConfig.GOOGLE_MAP_API_KEY)
        }
    }

    private fun initViews() {
        originAutocompleteFragment = childFragmentManager
            .findFragmentById(R.id.origin_autocomplete_fragment) as AutocompleteSupportFragment
        destinationAutocompleteFragment = childFragmentManager
            .findFragmentById(R.id.destination_autocomplete_fragment) as AutocompleteSupportFragment

        originAutocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )
        destinationAutocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )

        originAutocompleteFragment.setHint(getString(R.string.search_on_your_origin))
        destinationAutocompleteFragment.setHint(getString(R.string.search_on_your_destination))
    }

    private fun initListeners() {
        originAutocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.latLng?.apply {
                    origin = Address(place.name, latitude, longitude)
                }
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.an_error_occurred),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })

        destinationAutocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.latLng?.apply {
                    destination = Address(place.name, latitude, longitude)
                }
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.an_error_occurred), Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
    }

    override fun onCreateDetailPaneNavHostFragment(): NavHostFragment {
        return NavHostFragment.create(R.navigation.direction_nav)
    }

    override fun onListPaneViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onListPaneViewCreated(view, savedInstanceState)

        binding.directionsButton.setOnClickListener {
            if (!::origin.isInitialized) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_select_the_origin_address),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (!::destination.isInitialized) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.please_select_the_destination_address),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            openDetails(sourceAddress = origin, destinationAddress = destination)
        }
    }

    private fun openDetails(sourceAddress: Address, destinationAddress: Address) {
        val detailNavController = detailPaneNavHostFragment.navController

        val bundle = Bundle()
        bundle.putParcelable("sourceAddress", sourceAddress)
        bundle.putParcelable("destinationAddress", destinationAddress)

        detailNavController.navigate(R.id.mapFragment, bundle)

        slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED
        slidingPaneLayout.open()
    }
}