package net.estemon.studio.loccat.model

import org.json.JSONException
import org.json.JSONObject

fun parseCoordinates(
    qrValue: String?
) : TargetData? {
    return try {
        val jsonObject = qrValue?.let { JSONObject(it) }
        val latitude = jsonObject?.getDouble("latitude")
        val longitude = jsonObject?.getDouble("longitude")
        val hint = jsonObject?.getString("hint")
        TargetData(latitude, longitude, hint)
    } catch (e: JSONException) {
        null
    }
}

data class TargetData(
    val latitude: Double?,
    val longitude: Double?,
    val hint: String?
)