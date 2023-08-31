package com.example.widgets

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.widgets.model.QrCodeData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyser(
    val callback: (QrCodeData) -> Unit,
) : ImageAnalysis.Analyzer {
    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        val scanner = BarcodeScanning.getClient(options)
        val mediaImage = imageProxy.image
        mediaImage?.let {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image).addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode?.rawValue?.let { qrCodeJson ->
                        Log.d("QRCODE_JSON_STRING", qrCodeJson)
                        callback(fromJsonString(qrCodeJson))
                    }
                }
                // coming here means no satisfiable barcode was found
            }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                }
        }
        imageProxy.close()
    }
}

inline fun <reified T> fromJsonString(json: String): T = Gson().fromJson(json, object : TypeToken<T>() {}.type)
