package net.estemon.studio.loccat.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import net.estemon.studio.loccat.ui.screen.createJson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRDialog(
    markerPosition: LatLng,
    onDismiss: () -> Unit,
    onGenerateQR: (String) -> Unit
) {
    var hintText by remember { mutableStateOf("") }
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier.wrapContentWidth().wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("QR IMAGE")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Latitude: ${markerPosition.latitude}")
                Text("Longitude: ${markerPosition.longitude}")
                OutlinedTextField(
                    value = hintText,
                    onValueChange = { hintText = it },
                    label = { Text("Hint: ") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                SnackbarHost(hostState = snackbarHostState)
                TextButton(
                    onClick = {
                        focusManager.clearFocus()
                        if (hintText.isNotBlank()) {
                            val jsonData = createJson(markerPosition, hintText)
                            qrBitmap = QRCodeGenerator(jsonData)
                            onGenerateQR(hintText)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "You must provide a hint!"
                                )
                            }
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Generate")
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (qrBitmap != null) {
                    qrBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier
                                .size(200.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = {
                                val qrData = createJson(markerPosition, hintText)
                                checkQRCode(qrData)
                            }) {
                                Text("Check it!")
                            }
                            Button(onClick = {
                                saveQRCode(bitmap, context)
                            }) {
                                Text("Save")
                            }
                            Button(onClick = {
                                shareQRCode(bitmap, context)
                            }) {
                                Text("Share")
                            }
                        }
                    }
                }
            }
        }
    }
}