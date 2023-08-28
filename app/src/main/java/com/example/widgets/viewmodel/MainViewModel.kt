package com.example.widgets.viewmodel

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.widgets.model.Providers


class MainViewModel : ViewModel() {

    private val _providerInfo = mutableStateListOf<Providers>()
    val providerInfo : SnapshotStateList<Providers> = _providerInfo

    private var _packageName = ""
    val packageName = _packageName

    private val _installedPackages = mutableStateListOf<String>()
    val installedPackages : SnapshotStateList<String> = _installedPackages

    fun processPackages(context: Context) {
        _installedPackages.clear()
        val appWidgetManager = AppWidgetManager.getInstance(context)
        for (widgetProvider in appWidgetManager.installedProviders) {
            val packageName: String = widgetProvider.provider.packageName
            if (_installedPackages.contains(packageName).not()) {
                _installedPackages.add(packageName)
            }
        }
    }

    private fun processWidgetProviders(providers: MutableList<AppWidgetProviderInfo>) {
        for (provider in providers) {
            _providerInfo.add(Providers(false, provider))
        }
    }

    fun setSelectionStatus(index: Int, isSelected: Boolean) {
        _providerInfo[index] = _providerInfo[index].copy(isChecked = isSelected)
    }

    fun getInstalledProvidersForPackage(context: Context, packageName: String) {
        _packageName = packageName
        _providerInfo.clear()
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

    fun processSelectedWidget() = _providerInfo.filter { it.isChecked }
}