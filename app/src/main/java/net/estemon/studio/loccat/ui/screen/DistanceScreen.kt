package net.estemon.studio.loccat.ui.screen

import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import net.estemon.studio.loccat.Routes
import net.estemon.studio.loccat.model.parseCoordinates

@Composable
fun DistanceScreen(
    navController: NavHostController,
    qrValue: String?
) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var currentLocation by remember { mutableStateOf<Location?>(null) }
    var distanceInMeters by remember { mutableStateOf<Float?>(null) }

    // Parse coordinates
    val targetCoordinates = remember(qrValue) { parseCoordinates(qrValue) }
    if (targetCoordinates == null) {
        Text(text = "El código QR no contiene coordenadas válidas")
        return
    }

    val (targetLatitude, targetLongitude) = targetCoordinates

    // Create Location instance for goal point
    val targetLocation = remember {
        Location("target").apply {
            if (targetLatitude != null) {
                latitude = targetLatitude
            }
            if (targetLongitude != null) {
                longitude = targetLongitude
            }
        }
    }

    // Solicitar permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permiso concedido, no es necesario hacer nada aquí
            } else {
                // Manejar caso de permiso denegado
                // Mostrar mensaje o navegar atrás
            }
        }
    )

    // Verificar permisos y solicitar actualizaciones de ubicación
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicitar permiso
            locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Crear LocationRequest
    val locationRequest = remember {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).apply {
            setMinUpdateIntervalMillis(1000L)
            setMaxUpdateDelayMillis(0)
        }.build()
    }

    // Iniciar actualizaciones de ubicación
    LaunchedEffect(fusedLocationClient, locationRequest) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                currentLocation = locationResult.lastLocation
                distanceInMeters = currentLocation?.distanceTo(targetLocation)
                Log.d("DistanceScreen", "Ubicación actualizada: $currentLocation")
                Log.d("DistanceScreen", "Distancia calculada: $distanceInMeters")
            }
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    DisposableEffect(Unit) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                currentLocation = locationResult.lastLocation
                distanceInMeters = currentLocation?.distanceTo(targetLocation)
                Log.d("DistanceScreen", "Ubicación actualizada: $currentLocation")
                Log.d("DistanceScreen", "Distancia calculada: $distanceInMeters")
            }
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    // Mostrar distancia en la UI
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (distanceInMeters != null) {
            Text(text = "Distancia al objetivo: ${distanceInMeters!!} metros")

            // Navegar a HINT_SCREEN si está a menos de 1 metro
            if (distanceInMeters!! < 1) {
                // Navegar a la siguiente pantalla
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.HINT_SCREEN) {
                        popUpTo(Routes.DISTANCE_SCREEN) { inclusive = true }
                    }
                }
            }
        } else {
            Text(text = "Calculando distancia...")
        }
        // Información de depuración
        Text(text = "DEBUG: currentLocation = $currentLocation")
        Text(text = "DEBUG: targetLocation = $targetLocation")
        if (qrValue != null) {
            Text(text = "DEBUG qrValue: $qrValue")
        }
    }
}


