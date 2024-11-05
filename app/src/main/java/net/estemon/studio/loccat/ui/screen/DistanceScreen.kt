package net.estemon.studio.loccat.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import net.estemon.studio.loccat.Routes
import net.estemon.studio.loccat.model.parseCoordinates
import net.estemon.studio.loccat.viewmodel.DistanceViewModel
import net.estemon.studio.loccat.viewmodel.DistanceViewModelFactory

@Composable
fun DistanceScreen(
    navController: NavHostController,
    qrValue: String?
) {
    val context = LocalContext.current

    // get viewmodel instance
    val viewModel: DistanceViewModel = viewModel(
        factory = DistanceViewModelFactory(
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context),
            context = context
        )
    )

    // observe viewmodel states
    val distanceInMeters by viewModel.distanceInMeters
    val permissionGranted by viewModel.permissionGranted

    // parse coordinates
    val targetCoordinates = remember(qrValue) { parseCoordinates(qrValue) }
    if (targetCoordinates == null) {
        Text(text = "QR doesn't have valid coordinates")
        return
    }
    val (targetLatitude, targetLongitude) = targetCoordinates

    // create location instance for goal point
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

    // set targetLocation on ViewModel
    LaunchedEffect(targetLocation) {
        viewModel.setTargetLocation(targetLocation)
    }

    // ask location permissions
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.setPermissionGranted(isGranted)
            if (isGranted) {
                viewModel.setPermissionGranted(isGranted)
            } else {
                // TODO handle permission not granted
            }
        }
    )

    // check permissions
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
            ) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            viewModel.setPermissionGranted(true)
        }
    }

    // show distances
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (distanceInMeters != null) {
            val formattedDistance = "%.1f".format(distanceInMeters)
            Text(text = "$formattedDistance meters")

            // go to HINT_SCREEN if it's under 2 meters
            if (distanceInMeters!! < 2) {
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.HINT_SCREEN) {
                        popUpTo(Routes.DISTANCE_SCREEN) { inclusive = true }
                    }
                }
            }
        } else {
            Text(text = "Calculating distance...")
        }
    }
}


