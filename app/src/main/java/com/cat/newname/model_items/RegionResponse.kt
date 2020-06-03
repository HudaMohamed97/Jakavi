package com.example.catapplication.models

import com.google.gson.annotations.SerializedName

data class RegionResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("active") val active: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("cities") val cities: List<Cities>
)






