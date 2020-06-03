package com.example.catapplication.models

import com.google.gson.annotations.SerializedName

data class ReasonsResponce(

    @SerializedName("data") val data: List<ReasonData>,
    @SerializedName("state") val state: Int
)