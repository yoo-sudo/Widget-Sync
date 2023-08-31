package com.example.widgets.viewmodel

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.os.Build
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.example.widgets.encodeToBase64
import com.example.widgets.getAppIcon
import com.example.widgets.getAppNameFromPackageName
import com.example.widgets.model.Providers
import com.example.widgets.model.QrCodeData
import com.example.widgets.model.WidgetInfo
import com.example.widgets.toWidget


class MainViewModel : ViewModel() {

    private val _providerInfo = mutableStateListOf<Providers>()
    val providerInfo: SnapshotStateList<Providers> = _providerInfo

    var selectPackageName = ""

    private val _installedPackages = mutableStateListOf<String>()
    val installedPackages: SnapshotStateList<String> = _installedPackages

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

    fun getInstalledProvidersForPackage(context: Context) {
        _providerInfo.clear()
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val installedProviders = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appWidgetManager.getInstalledProvidersForPackage(selectPackageName, null)
        } else {
            null
        }
        if (installedProviders != null) {
            processWidgetProviders(installedProviders)
        }
    }

    fun buildWidgetData(context: Context, qrCodeData: QrCodeData) = WidgetInfo(
        appName = getAppNameFromPackageName(context, selectPackageName),
        iconPreview = encodeToBase64(getAppIcon(context, selectPackageName).toBitmap()),
        widget = getSelectedWidgets().map { it.toWidget(context) },
        requestId = qrCodeData.requestId,
        uuid = qrCodeData.uuid,
        packageName = selectPackageName,
        customerId = qrCodeData.customerId
    )

    fun setSelectedPackageName(packageName: String) {
        selectPackageName = packageName
    }

    fun getSelectedWidgets() = _providerInfo.filter { it.isChecked }
}