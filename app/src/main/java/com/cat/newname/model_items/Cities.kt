package com.example.catapplication.models

import com.google.gson.annotations.SerializedName


data class Cities(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("active") val active: Int,
    @SerializedName("region_id") val region_id: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
)