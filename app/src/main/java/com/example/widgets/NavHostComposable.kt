package com.example.widgets

import android.app.Activity
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
            AppList(mainViewModel) {
                mainViewModel.getInstalledProvidersForPackage(activity, mainViewModel.installedPackages[it])
                navController.navigate("WidgetDetails")
            }
        }
        composable("WidgetDetails") {
            AppWidgetDetails(mainViewModel, activity) {
                navController.navigate("qrScanner")
            }
        }
        composable("qrScanner") {
            QrScanner()

        }
    }
}