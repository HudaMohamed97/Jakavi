package com.example.catapplication.models

import com.google.gson.annotations.SerializedName


data class CmlResponce(

    @SerializedName("data") val data: List<CmlData>,
    @SerializedName("state") val state: Int
)