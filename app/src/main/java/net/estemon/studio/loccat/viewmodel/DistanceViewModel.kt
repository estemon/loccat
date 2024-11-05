package net.estemon.studio.loccat.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class DistanceViewModel(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val context: Context
) : ViewModel() {

    private val _distanceInMeters = mutableStateOf<Float?>(null)
    val distanceInMeters: State<Float?> get() = _distanceInMeters

    private val _currentLocation = mutableStateOf<Location?>(null)
    val currentLocation: State<Location?> get() = _currentLocation

    private val _permissionGranted = mutableStateOf(false)
    val permissionGranted: State<Boolean> get() = _permissionGranted

    private var locationCallback: LocationCallback? = null
    private var targetLocation: Location? = null
    private var updatesStarted = false

    fun setTargetLocation(location: Location) {
        targetLocation = location
        maybeStartLocationUpdates()
    }

    fun setPermissionGranted(granted: Boolean) {
        _permissionGranted.value = granted
        if (granted) {
            startLocationUpdates()
        }
    }

    private fun maybeStartLocationUpdates() {
        if (_permissionGranted.value && targetLocation != null && !updatesStarted) {
            startLocationUpdates()
            updatesStarted = true
        }
    }

    private fun startLocationUpdates() {
        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).apply {
                setMinUpdateIntervalMillis(500L)
                setMaxUpdateDelayMillis(0)
        }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val currentLoc = locationResult.lastLocation
                _currentLocation.value = currentLoc
                if (currentLoc != null && targetLocation != null) {
                    _distanceInMeters.value = currentLoc.distanceTo(targetLocation!!)
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
            ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }
}

class DistanceViewModelFactory(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DistanceViewModel(fusedLocationClient, context) as T
    }
}