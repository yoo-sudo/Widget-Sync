package com.example.widgets

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.widgets.ui.theme.composable.AppList
import com.example.widgets.ui.theme.composable.AppWidgetDetails
import com.example.widgets.ui.theme.composable.QrScanner
import com.example.widgets.viewmodel.MainViewModel

@Composable
fun WidgetsHost(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    WidgetsNavHost(
        navController = navController,
        mainViewModel = mainViewModel
    )
}

@Composable
fun WidgetsNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel
) {
    val activity = (LocalContext.current as Activity)
    NavHost(navController = navController, startDestination = "AppList") {
        composable("AppList") {
            mainViewModel.processPackages(activity)
            AppList(mainViewModel) { index ->
                mainViewModel.setSelectedPackageName(mainViewModel.installedPackages[index])
                mainViewModel.getInstalledProvidersForPackage(activity)
                navController.navigate("WidgetDetails")
            }
        }
        composable("WidgetDetails") {
            AppWidgetDetails(mainViewModel, activity) {
                if (mainViewModel.getSelectedWidgets().isNotEmpty()) {
                    navController.navigate("qrScanner")
                } else {
                    Toast.makeText(activity, "Select widget to send", Toast.LENGTH_SHORT).show()
                }
            }
        }
        composable("qrScanner") {
            QrScanner { qrCodeData ->
                Log.d("XOXOXO", mainViewModel.buildWidgetData(activity, qrCodeData).customerId.toString())
            }
        }
    }
}