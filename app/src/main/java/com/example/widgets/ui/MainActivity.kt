package com.example.widgets.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.widgets.WidgetsHost
import com.example.widgets.ui.theme.WidgetsTheme
import com.example.widgets.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFECYCLE", "onCreate()")
        setContent {
            WidgetsTheme {
                WidgetsHost(mainViewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LIFECYCLE", "onDestroy()")
    }
}