package net.estemon.studio.loccat.ui.screen

import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import net.estemon.studio.loccat.Routes
import net.estemon.studio.loccat.model.QRCodeAnalyzer

@Composable
fun QRScannerScreen(navController: NavHostController) {
    var hasCameraPermission by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
        }
    )

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                hasCameraPermission = true
            }
            else -> {
                cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    if (hasCameraPermission) {
        // Show camera and controller
        CameraPreview(navController = navController)
    } else {
        // Show a message asking for permission
        Text(text = "Grant permission to scan QR Codes.")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "QR SCANNER")
    }
}

@Composable
fun CameraPreview(navController: NavHostController) {
    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val executor = remember { ContextCompat.getMainExecutor(context) }

    val onQRCodeScanned: (String) -> Unit = { qrValue ->
        val encodedQrValue = Uri.encode(qrValue)
        navController.navigate("${Routes.DISTANCE_SCREEN}/$encodedQrValue")
    }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(executor, QRCodeAnalyzer(onQRCodeScanned))

            try {
                val cameraProvider = cameraProviderFuture.get()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}
