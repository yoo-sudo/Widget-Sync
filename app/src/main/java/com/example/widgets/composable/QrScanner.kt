package com.example.widgets.composable

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.widgets.BarcodeAnalyser
import com.example.widgets.model.ApiState
import com.example.widgets.model.QrCodeData
import com.example.widgets.viewmodel.MainViewModel
import java.util.concurrent.Executors

@OptIn(ExperimentalGetImage::class)
@Composable
fun QrScanner(mainViewModel: MainViewModel, onBackPressed: (Boolean) -> Unit) {
    BackHandler {
        onBackPressed.invoke(mainViewModel.response.value is ApiState.Success)
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProcessDataComposable(mainViewModel = mainViewModel)
        }
    }
}

@Composable
fun ScannerView(onScanComplete: (QrCodeData) -> Unit) {
    AndroidView(
        { context ->
            val cameraExecutor = Executors.newSingleThreadExecutor()
            val previewView = PreviewView(context).also {
                it.scaleType = PreviewView.ScaleType.FILL_CENTER
            }
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val imageCapture = ImageCapture.Builder().build()

                val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, BarcodeAnalyser { qrCodeData ->
                            onScanComplete(qrCodeData)
                            Toast.makeText(context, "Barcode found", Toast.LENGTH_SHORT).show()
                        })
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        context as ComponentActivity, cameraSelector, preview, imageCapture, imageAnalyzer
                    )

                } catch (exc: Exception) {
                    Log.e("DEBUG", "Use case binding failed", exc)
                }
            }, ContextCompat.getMainExecutor(context))
            previewView
        },
        modifier = Modifier.size(width = 250.dp, height = 250.dp)
    )
}

@Composable
fun ProcessDataComposable(mainViewModel: MainViewModel) {
    val context = LocalContext.current
    when (val result = mainViewModel.response.value) {
        is ApiState.Success -> {
            Text(text = "Widgets Successfully sent to TAM")
        }

        is ApiState.Failure -> {
            Text(text = "${result.msg}")
        }

        ApiState.Loading -> {
            CircularProgressIndicator()
        }

        ApiState.Empty -> {
            ScannerView { qrCodeData ->
                mainViewModel.widgetRequest(context, qrCodeData).also { request ->
                    Log.d("API_REQUEST", request.toString())
                    mainViewModel.sendDataToTam(request)
                }
            }
            Text(
                text = "Scan QR Code",
                modifier = Modifier.padding(top = 48.dp)
            )
        }
    }
}