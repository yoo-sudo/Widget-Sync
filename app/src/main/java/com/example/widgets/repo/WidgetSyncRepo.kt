package com.example.widgets.repo

import com.example.widgets.APIClient
import com.example.widgets.IWidgetSyncService
import com.example.widgets.model.Response
import com.example.widgets.model.WidgetRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call

class WidgetSyncRepo {

    private var apiClient: IWidgetSyncService? = null

    init {
        apiClient = APIClient.getApiClient().create(IWidgetSyncService::class.java)
    }

    fun sendWidgetDetails(request: WidgetRequest): Flow<Call<Response>> = flow {
        emit(apiClient!!.sendWidgetDetails(request))
    }.flowOn(Dispatchers.IO)
}