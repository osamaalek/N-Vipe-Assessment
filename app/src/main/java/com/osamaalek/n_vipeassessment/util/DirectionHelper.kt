package com.osamaalek.n_vipeassessment.util

import android.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONException
import org.json.JSONObject

@Throws(JSONException::class)
fun parseDirectionsResponse(jsonString: String): PolylineOptions {
    val jsonResult = JSONObject(jsonString)
    val routes = jsonResult.getJSONArray("routes")
    if (routes.length() > 0) {
        val route = routes.getJSONObject(0)
        val overviewPolyline = route.getJSONObject("overview_polyline")
        val decodedPath = decodePolyline(overviewPolyline.getString("points"))

        return PolylineOptions().addAll(decodedPath)
            .color(Color.RED)
            .width(5f)
    } else {
        throw JSONException("No routes found")
    }
}

fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val p = LatLng(
            lat.toDouble() / 1E5,
            lng.toDouble() / 1E5
        )
        poly.add(p)
    }

    return poly
}

@Throws(JSONException::class)
fun calculateTravelTime(jsonString: String): String {
    val jsonResult = JSONObject(jsonString)
    val routes = jsonResult.getJSONArray("routes")
    if (routes.length() > 0) {
        val route = routes.getJSONObject(0)
        val legs = route.getJSONArray("legs")
        if (legs.length() > 0) {
            val leg = legs.getJSONObject(0)
            return formatTravelTime(leg.getJSONObject("duration").getLong("value") / 60)
        }
    }
    return ""
}