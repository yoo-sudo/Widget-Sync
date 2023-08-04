package com.example.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.widgets.model.Providers

class MainViewModel: ViewModel() {

    private val _installedProviders = mutableStateListOf<Providers>()
    val installedProviders get() = _installedProviders

    fun processProviders(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        for (provider in appWidgetManager.installedProviders) {
            _installedProviders.add(Providers(false, provider))

        }
    }

    fun setSelectionStatus(index: Int, isSelected: Boolean) {
        _installedProviders[index] = _installedProviders[index].copy(isChecked = isSelected)
    }
}