package com.osamaalek.n_vipeassessment.repository

import com.osamaalek.n_vipeassessment.BuildConfig
import com.osamaalek.n_vipeassessment.model.Address
import com.osamaalek.n_vipeassessment.util.DirectionApi

class DirectionsRepository {
    suspend fun getDirections(origin: Address, destination: Address): String {
        val task = DirectionApi(
            apiKey = BuildConfig.GOOGLE_MAP_API_KEY,
            origin = origin,
            destination = destination
        )
        try {
            return task.fetchDirectionsData()
        } catch (e: Exception) {
            throw e
        }
    }
}
