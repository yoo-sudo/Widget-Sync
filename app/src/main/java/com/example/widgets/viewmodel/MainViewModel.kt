package com.example.widgets.viewmodel

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.widgets.model.Providers

class MainViewModel: ViewModel() {

    private val _installedProviders = mutableStateListOf<Providers>()
    val installedProviders get() = _installedProviders

    private var _packageName = ""
    val packageName get() = _packageName

    private val _installedPackages = mutableStateListOf<ApplicationInfo>()
    val installedPackages get() = _installedPackages

    fun processPackages(context: Context) {
        _installedPackages.clear()
        val packageManager = context.packageManager
        val packages: List<ApplicationInfo> = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        for (i in packages.indices) {
            if(packageManager.getLaunchIntentForPackage(packages[i].packageName) != null){
                _installedPackages.add(packages[i])
            }
        }
    }

    private fun processWidgetProviders(providers: MutableList<AppWidgetProviderInfo>) {
        for (provider in providers) {
            _installedProviders.add(Providers(false, provider))
        }
    }

    fun setSelectionStatus(index: Int, isSelected: Boolean) {
        _installedProviders[index] = _installedProviders[index].copy(isChecked = isSelected)
    }

    fun getInstalledProvidersForPackage(context: Context, packageName: String) {
        _packageName = packageName
        _installedProviders.clear()
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val installedProviders = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appWidgetManager.getInstalledProvidersForPackage(packageName, null)
        } else {
            null
        }
        if (installedProviders != null) {
            processWidgetProviders(installedProviders)
        }
    }

    fun processSelectedWidget() = _installedProviders.filter { it.isChecked }
}