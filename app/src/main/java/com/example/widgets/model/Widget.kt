package com.example.widgets.model

import com.google.gson.annotations.SerializedName

data class Widget(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("provider")
    val provider: String? = null,

    @SerializedName("ratio")
    val ratio: String? = null,

    @SerializedName("review")
    val preview: String? = null,

    @SerializedName("extras")
    val extras: String? = null,
)