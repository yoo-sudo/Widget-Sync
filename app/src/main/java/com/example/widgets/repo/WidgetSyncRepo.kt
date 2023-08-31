package com.example.widgets.repo

import com.example.widgets.RetrofitInstance
import com.example.widgets.model.WidgetInfo

class WidgetSyncRepo {

    private val creditCardService = RetrofitInstance.widgetSyncService

    suspend fun sendWidgetInfos(): List<WidgetInfo> {
        return creditCardService.sendWidgetInfos()
    }
}