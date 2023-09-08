package com.example.widgets.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import com.example.widgets.WidgetsHost
import com.example.widgets.composable.PermissionScreen
import com.example.widgets.ui.theme.WidgetsTheme
import com.example.widgets.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private val permissionState = mutableStateOf(false)

    companion object {
        private const val CAMERA = Manifest.permission.CAMERA
    }

    // Register the contract in your fragment/activity and update state based on the result
    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            permissionState.value = granted
            if (!granted) {
                Toast.makeText(
                    this, "Cannot Proceed! Please grant permission through setting", Toast.LENGTH_LONG
                ).show()
            }
        }

    private val showAlertDialog: MutableState<Boolean> by lazy {
        mutableStateOf(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFECYCLE", "onCreate()")
        checkIfPermissionGranted()
        setContent {
            setContent {
                WidgetsTheme {
                    if (permissionState.value) {
                        WidgetsHost(mainViewModel)
                    } else {
                        PermissionScreen(
                            showAlert = showAlertDialog.value,
                            onClick = {
                                checkIfPermissionGranted()
                            },
                            onRationaleReply = { accepted ->
                                if (accepted) {
                                    permissionRequest.launch(CAMERA)
                                }
                            })
                    }
                }
            }
        }
    }

    private fun checkIfPermissionGranted() {
        when {
            isPermissionGranted() -> {
                permissionState.value = true
            }
            else -> {
                permissionRequest.launch(CAMERA)
            }
        }
    }

    private fun isPermissionGranted() = ContextCompat.checkSelfPermission(
        this, CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LIFECYCLE", "onDestroy()")
    }
}