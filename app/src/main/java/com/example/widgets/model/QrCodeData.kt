package com.example.widgets.model

import com.google.gson.annotations.SerializedName

data class QrCodeData(
    @SerializedName("category")
    val category: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("requestId")
    val requestId: String? = null,

    @SerializedName("uuid")
    val uuid: String? = null,

    @SerializedName("customerId")
    val customerId: String? = null,
)