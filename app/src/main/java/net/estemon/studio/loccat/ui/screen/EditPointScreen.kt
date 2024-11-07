package net.estemon.studio.loccat.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.widgets.ScaleBar
import net.estemon.studio.loccat.ui.components.QRDialog
import org.json.JSONObject

fun createJson(latLng: LatLng, hint: String) : String {
    val jsonObject = JSONObject().apply {
        put("latitude", latLng.latitude)
        put("longitude", latLng.longitude)
        put("hint", hint)
    }
    return jsonObject.toString()
}

@Composable
fun EditPointScreen(navController: NavHostController) {

    val initialPosition = LatLng(41.515233, 1.650557)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 10f)
    }
    val uiSettings = MapUiSettings(
        mapToolbarEnabled = false
    )

    var markerPosition by remember { mutableStateOf(initialPosition) }
    var hintText by remember { mutableStateOf("") }
    var jsonData by remember { mutableStateOf("") }
    var isMapLoaded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLng ->
                markerPosition = latLng
            },
            onMapLoaded = { isMapLoaded = true },
            uiSettings = uiSettings
        ) {
            markerPosition.let {
                val markerState = remember(markerPosition) {
                    MarkerState(position = markerPosition)
                }
                Marker(
                    state = markerState,
                        title = "Lat: ${markerPosition.latitude}, Lon: ${markerPosition.longitude}",
                        snippet = null,
                        draggable = false
                )
            }
        }
        ScaleBar(
            modifier = Modifier
                .padding(top = 5.dp, end = 15.dp)
                .align(Alignment.TopEnd),
            cameraPositionState = cameraPositionState
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Generate QR"
                )
            }
        }
        }

    if (showDialog) {
        QRDialog(
            markerPosition = markerPosition,
            onDismiss = { showDialog = false },
            onGenerateQR = { }
        )
    }
}

