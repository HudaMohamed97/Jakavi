package com.example.catapplication.models

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: Int,
    @SerializedName("address") val address: String,
    @SerializedName("player_id") val player_id: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("photo") val photo: String,
    @SerializedName("account_type") val account_type: String,
    @SerializedName("flm_manager") val flm_manager: String,
    @SerializedName("score") val score: Int,
    @SerializedName("region_name") val region_name: String,
    @SerializedName("city_name") val city_name: String,
    @SerializedName("target") val target: Int,
    @SerializedName("percentage") val percentage: Int
)
