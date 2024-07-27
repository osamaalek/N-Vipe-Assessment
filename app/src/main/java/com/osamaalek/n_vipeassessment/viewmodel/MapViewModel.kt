package com.osamaalek.n_vipeassessment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.PolylineOptions
import com.osamaalek.n_vipeassessment.model.Address
import com.osamaalek.n_vipeassessment.repository.DirectionsRepository
import com.osamaalek.n_vipeassessment.util.calculateTravelTime
import com.osamaalek.n_vipeassessment.util.parseDirectionsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(private val directionsRepository: DirectionsRepository) :
    ViewModel() {

    private val _polylineOptionsState: MutableStateFlow<PolylineOptions> =
        MutableStateFlow(PolylineOptions())
    val polylineOptionsState: StateFlow<PolylineOptions> get() = _polylineOptionsState.asStateFlow()

    private val _travelTileState: MutableStateFlow<String> = MutableStateFlow("")
    val travelTileState: StateFlow<String> get() = _travelTileState.asStateFlow()

    private val _loadingState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> get() = _loadingState.asStateFlow()

    private val _errorState: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorState: StateFlow<String?> get() = _errorState.asStateFlow()

    fun fetchDirections(origin: Address, destination: Address) {
        viewModelScope.launch {
            _polylineOptionsState.emit(PolylineOptions())
            _loadingState.emit(true)
            _errorState.emit(null)
            try {
                val response =
                    directionsRepository.getDirections(origin = origin, destination = destination)
                _polylineOptionsState.emit(parseDirectionsResponse(response))
                _travelTileState.emit(calculateTravelTime(response))
            } catch (e: Exception) {
                _errorState.emit(e.message)
            } finally {
                _loadingState.emit(false)
            }
        }
    }
}