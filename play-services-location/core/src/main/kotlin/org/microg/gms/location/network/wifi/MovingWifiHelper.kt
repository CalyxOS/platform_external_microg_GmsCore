/*
 * SPDX-FileCopyrightText: 2023 microG Project Team
 * SPDX-License-Identifier: Apache-2.0
 */

package org.microg.gms.location.network.wifi

import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_WIFI
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.core.content.getSystemService
import androidx.core.location.LocationCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.microg.gms.location.network.TAG
import java.net.HttpURLConnection
import java.net.URL

private val MOVING_WIFI_HOTSPOTS = setOf(
    // Austria
    "OEBB",
    "Austrian Flynet",
    // Belgium
    "THALYSNET",
    // Canada
    "Air Canada",
    // Czech Republic
    "CDWiFi",
    // France
    "_SNCF_WIFI_INOUI",
    "_SNCF_WIFI_INTERCITES",
    "OUIFI",
    "NormandieTrainConnecte",
    // Germany
    "WIFIonICE",
    "WIFI@DB",
    "WiFi@DB",
    "RRX Hotspot",
    "FlixBus Wi-Fi",
    "FlyNet",
    "Telekom_FlyNet",
    "Vestische WLAN",
    // Hungary
    "MAVSTART-WIFI",
    // Netherlands
    "KEOLIS Nederland",
    // United Kingdom
    "CrossCountryWiFi",
    "GWR WiFi",
    // United States
    "Amtrak_WiFi",
)

private val PHONE_HOTSPOT_KEYWORDS = setOf(
    "iPhone",
    "Galaxy",
    "AndroidAP"
)

/**
 * A Wi-Fi hotspot that changes its location dynamically and thus is unsuitable for use with location services that assume stable locations.
 *
 * Some moving Wi-Fi hotspots allow to determine their location when connected or through a public network API.
 */
val WifiDetails.isMoving: Boolean
    get() {
        if (open && MOVING_WIFI_HOTSPOTS.contains(ssid)) {
            return true
        }
        if (PHONE_HOTSPOT_KEYWORDS.any { ssid?.contains(it) == true }) {
            return true
        }
        return false
    }

class MovingWifiHelper(private val context: Context) {
    suspend fun retrieveMovingLocation(current: WifiDetails): Location {
        if (!isLocallyRetrievable(current)) throw IllegalArgumentException()
        val connectivityManager = context.getSystemService<ConnectivityManager>() ?: throw IllegalStateException()
        val url = URL(MOVING_WIFI_HOTSPOTS_LOCALLY_RETRIEVABLE[current.ssid])
        return withContext(Dispatchers.IO) {
            val network = if (isLocallyRetrievable(current) && SDK_INT >= 23) {
                @Suppress("DEPRECATION")
                (connectivityManager.allNetworks.singleOrNull {
                    val networkInfo = connectivityManager.getNetworkInfo(it)
                    Log.d(TAG, "Network info: $networkInfo")
                    networkInfo?.type == TYPE_WIFI && networkInfo.isConnected
                })
            } else {
                null
            }
            val connection = (if (SDK_INT >= 21) {
                network?.openConnection(url)
            } else {
                null
            } ?: url.openConnection()) as HttpURLConnection
            try {
                connection.doInput = true
                if (connection.responseCode != 200) throw RuntimeException("Got error")
                parseInput(current.ssid!!, connection.inputStream.readBytes())
            } finally {
                connection.inputStream.close()
                connection.disconnect()
            }
        }
    }

    private fun parseWifiOnIce(location: Location, data: ByteArray): Location {
        val json = JSONObject(data.decodeToString())
        if (json.getString("gpsStatus") != "VALID") throw RuntimeException("GPS not valid")
        location.accuracy = 100f
        location.time = json.getLong("serverTime") - 15000L
        location.latitude = json.getDouble("latitude")
        location.longitude = json.getDouble("longitude")
        json.optDouble("speed").takeIf { !it.isNaN() }?.let {
            location.speed = (it / 3.6).toFloat()
            LocationCompat.setSpeedAccuracyMetersPerSecond(location, location.speed * 0.1f)
        }
        return location
    }

    private fun parseFlixbus(location: Location, data: ByteArray): Location {
        val json = JSONObject(data.decodeToString())
        location.accuracy = 100f
        location.latitude = json.getDouble("latitude")
        location.longitude = json.getDouble("longitude")
        json.optDouble("speed").takeIf { !it.isNaN() }?.let {
            location.speed = it.toFloat()
            LocationCompat.setSpeedAccuracyMetersPerSecond(location, location.speed * 0.1f)
        }
        return location
    }

    private fun parsePassengera(location: Location, data: ByteArray): Location {
        val json = JSONObject(data.decodeToString())
        location.accuracy = 100f
        location.latitude = json.getDouble("gpsLat")
        location.longitude = json.getDouble("gpsLng")
        json.optDouble("speed").takeIf { !it.isNaN() }?.let {
            location.speed = (it / 3.6).toFloat()
            LocationCompat.setSpeedAccuracyMetersPerSecond(location, location.speed * 0.1f)
        }
        json.optDouble("altitude").takeIf { !it.isNaN() }?.let { location.altitude = it }
        return location
    }

    private fun parseInput(ssid: String, data: ByteArray): Location {
        val location = Location(ssid)
        return when (ssid) {
            "WIFIonICE" -> parseWifiOnIce(location, data)
            "FlixBus Wi-Fi" -> parseFlixbus(location, data)
            "MAVSTART-WIFI" -> parsePassengera(location, data)
            else -> throw UnsupportedOperationException()
        }
    }

    fun isLocallyRetrievable(wifi: WifiDetails): Boolean =
        MOVING_WIFI_HOTSPOTS_LOCALLY_RETRIEVABLE.containsKey(wifi.ssid)

    companion object {
        private val MOVING_WIFI_HOTSPOTS_LOCALLY_RETRIEVABLE = mapOf(
            "WIFIonICE" to "https://iceportal.de/api1/rs/status",
            "FlixBus Wi-Fi" to "https://media.flixbus.com/services/pis/v1/position",
            "MAVSTART-WIFI" to "http://portal.mav.hu/portal/api/vehicle/realtime"
        )
    }
}

