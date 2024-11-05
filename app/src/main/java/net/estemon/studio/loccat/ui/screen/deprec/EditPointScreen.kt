package net.estemon.studio.loccat.ui.screen.deprec

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import net.estemon.studio.loccat.Routes
import org.json.JSONObject

@Composable
fun EditPointScreen2(navController: NavHostController) {
    val context = LocalContext.current

    // marker position state
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }

    // initial position state
    val initialPosition = LatLng(41.515233, 1.650557)

    // hint text state
    var hintText by remember { mutableStateOf("") }

    // json data with latLng + hint
    var jsonData by remember { mutableStateOf("") }

    // map camera controller
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 10f)
    }

    var isMapLoaded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    markerPosition = latLng
                },
                onMapLoaded = { isMapLoaded = true }
            ) {
                markerPosition?.let { position ->
                    val markerState = rememberMarkerState(position = position)
                    MarkerOptions()
                        .position(initialPosition)
                        .title("Marker at home")
                    Marker(
                        state = markerState,
                        title = "Selected mark",
                        draggable = true
                    )
                }
            }
        }

        // input fields
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = hintText,
                onValueChange = { hintText = it },
                label = { Text("Hint") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (markerPosition != null && hintText.isNotBlank()) {
                        // jsonData = createJson(markerPosition!!, hintText)
                    } else {
                        // TODO error message
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Generate QR")
            }
        }
        Text(text = "EDIT POINT")
        Text(text = jsonData)
    }
}

//fun createJson(latLng: LatLng, hint: String) : String {
//    val jsonObject = JSONObject().apply {
//        put("latitude", latLng.latitude)
//        put("longitude", latLng.longitude)
//        put("hint", hint)
//    }
//    return jsonObject.toString()
//}