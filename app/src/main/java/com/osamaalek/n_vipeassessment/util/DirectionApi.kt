package com.osamaalek.n_vipeassessment.util

import com.osamaalek.n_vipeassessment.model.Address
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.io.IOException

class DirectionApi(
    private val apiKey: String,
    private val origin: Address,
    private val destination: Address,
    private val okHttpClient: OkHttpClient = OkHttpClient()
) {

    suspend fun fetchDirectionsData(): String {
        return try {
            withContext(Dispatchers.IO) {
                fetchDirectionsDataFromApi()
            }
        } catch (e: IOException) {
            throw Exception("Network error while fetching directions data", e)
        } catch (e: JSONException) {
            throw Exception("Error while parsing directions response", e)
        } catch (e: Exception) {
            throw Exception("Unexpected error", e)
        }
    }

    @Throws(IOException::class)
    private fun fetchDirectionsDataFromApi(): String {
        val request = Request.Builder()
            .url(buildDirectionsApiUrl())
            .build()

        val response: Response = okHttpClient.newCall(request).execute()
        return try {
            if (!response.isSuccessful) {
                throw IOException("Unexpected response code: ${response.code()}")
            }
            response.body()?.string() ?: throw IOException("Empty response body")
        } finally {
            response.body()?.close()
        }
    }

    private fun buildDirectionsApiUrl(): String {
        val baseUrl = "https://maps.googleapis.com/maps/api/directions/json"
        val originStr = "${origin.latitude},${origin.longitude}"
        val destinationStr = "${destination.latitude},${destination.longitude}"

        return "$baseUrl?origin=$originStr&destination=$destinationStr&mode=walking&key=$apiKey"
    }
}
