package com.example.widgets

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.widgets.composable.AppList
import com.example.widgets.composable.AppWidgetDetails
import com.example.widgets.composable.QrScanner
import com.example.widgets.viewmodel.MainViewModel

@Composable
fun WidgetsHost(mainViewModel: MainViewModel) {
    val navController = rememberNavController()
    WidgetsNavHost(navController = navController, mainViewModel = mainViewModel)
}

@Composable
fun WidgetsNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
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
            AppWidgetDetails(mainViewModel) {
                if (mainViewModel.getSelectedWidgets().isNotEmpty()) {
                    navController.navigate("qrScanner")
                } else {
                    Toast.makeText(activity, "Select widget to send", Toast.LENGTH_SHORT).show()
                }
            }
        }
        composable("qrScanner") {
            QrScanner(mainViewModel) { widgetDataSent ->
                if (widgetDataSent) {
                    navController.popBackStack("AppList", false)
                } else {
                    navController.popBackStack()
                }
            }
        }
    }
}