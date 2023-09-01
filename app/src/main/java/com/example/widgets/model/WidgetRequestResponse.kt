package com.example.widgets.model

import com.google.gson.annotations.SerializedName

data class WidgetRequest(
    @SerializedName("appName")
    val appName: String? = null,

    @SerializedName("packageName")
    val packageName: String? = null,

    @SerializedName("iconPreview")
    val iconPreview: String? = null,

    @SerializedName("widget")
    var widget: List<Widget>? = null,

    @SerializedName("requestId")
    val requestId: String? = null,

    @SerializedName("uuid")
    val uuid: String? = null,

    @SerializedName("customerId")
    val customerId: String? = null,
)

data class Response(val status: String? = null, val message: String? = null)