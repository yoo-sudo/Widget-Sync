package com.example.widgets.model

import com.google.gson.annotations.SerializedName

data class WidgetInfo(
    @SerializedName("category")
    val category: String? = "widget_upload",

    @SerializedName("appName")
    val appName: String? = null,

    @SerializedName("requestId")
    val requestId: String? = null,

    @SerializedName("uuid")
    val uuid: String? = null,

    @SerializedName("customerId")
    val customerId: String? = null,

    @SerializedName("packageName")
    val packageName: String? = null,

    @SerializedName("iconPreview")
    val iconPreview: String? = null,

    @SerializedName("widget")
    var widget: List<Widget>? = null
)
