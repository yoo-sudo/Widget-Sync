package com.example.widgets.viewmodel

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.os.Build
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.widgets.encodeToBase64
import com.example.widgets.getAppIcon
import com.example.widgets.getAppNameFromPackageName
import com.example.widgets.model.ApiState
import com.example.widgets.model.Providers
import com.example.widgets.model.QrCodeData
import com.example.widgets.model.WidgetRequest
import com.example.widgets.repo.WidgetSyncRepo
import com.example.widgets.toWidget
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val repository = WidgetSyncRepo()

    private val _providerInfo = mutableStateListOf<Providers>()
    val providerInfo: SnapshotStateList<Providers> = _providerInfo

    private val _installedPackages = mutableStateListOf<String>()
    val installedPackages: SnapshotStateList<String> = _installedPackages

    val response: MutableState<ApiState> = mutableStateOf(ApiState.Empty)

    var SelectedAppPckName = ""

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
            appWidgetManager.getInstalledProvidersForPackage(SelectedAppPckName, null)
        } else {
            null
        }
        if (installedProviders != null) {
            processWidgetProviders(installedProviders)
        }
    }

    fun sendDataToTam(request: WidgetRequest) {
        viewModelScope.launch {
            repository.sendWidgetDetails(request).onStart {
                response.value = ApiState.Loading
            }.catch {
                response.value = ApiState.Failure(it)
            }.collect {
                it.enqueue(object : Callback<com.example.widgets.model.Response> {
                    override fun onResponse(call: Call<com.example.widgets.model.Response>, res: Response<com.example.widgets.model.Response>) {
                        if (res.code() == 200) {
                            response.value = ApiState.Success("Success")
                        } else {
                            response.value = ApiState.Success("Failed")
                        }
                    }

                    override fun onFailure(call: Call<com.example.widgets.model.Response>, t: Throwable) {
                        response.value = ApiState.Failure(t)
                    }
                })
            }
        }
    }

    fun widgetRequest(context: Context, qrCodeData: QrCodeData) = WidgetRequest(
        appName = getAppNameFromPackageName(context, SelectedAppPckName),
        iconPreview = encodeToBase64(getAppIcon(context, SelectedAppPckName).toBitmap()),
        widget = getSelectedWidgets().map { it.toWidget(context) },
        requestId = qrCodeData.requestId,
        uuid = qrCodeData.uuid,
        packageName = SelectedAppPckName,
        customerId = qrCodeData.customerId
    )

    fun setSelectedPackageName(packageName: String) {
        SelectedAppPckName = packageName
    }

    fun getSelectedWidgets() = _providerInfo.filter { it.isChecked }
}