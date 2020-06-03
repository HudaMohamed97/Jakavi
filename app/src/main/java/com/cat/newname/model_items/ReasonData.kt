package com.example.catapplication.models

import com.google.gson.annotations.SerializedName


data class ReasonData(

    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)