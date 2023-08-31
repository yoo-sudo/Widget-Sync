package com.example.widgets

import com.example.widgets.model.WidgetInfo
import retrofit2.http.PUT

fun interface WidgetSyncService {

    @PUT("credit_cards")
    suspend fun sendWidgetInfos(): List<WidgetInfo>
}