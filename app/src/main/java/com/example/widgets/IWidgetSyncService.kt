package com.example.widgets

import com.example.widgets.model.Response
import com.example.widgets.model.WidgetRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

fun interface IWidgetSyncService {

    @POST("widgetUpload")
    fun sendWidgetDetails(@Body request: WidgetRequest): Call<Response>
}