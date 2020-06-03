package com.example.catapplication.models

import com.google.gson.annotations.SerializedName


data class DataResponse(
    @SerializedName("data") val data: Data,
    @SerializedName("msg") val message: String,
    @SerializedName("state") val state: Int
)