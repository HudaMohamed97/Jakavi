package com.example.catapplication.models

import com.google.gson.annotations.SerializedName

data class AllPatientModel(
    @SerializedName("current_page") val current_page: Int,
    @SerializedName("data") val data: List<PatientRepData>,
    @SerializedName("from") val from: Int,
    @SerializedName("last_page") val last_page: Int,
    @SerializedName("next_page_url") val next_page_url: String,
    @SerializedName("path") val path: String,
    @SerializedName("per_page") val per_page: Int,
    @SerializedName("prev_page_url") val prev_page_url: String,
    @SerializedName("to") val to: Int,
    @SerializedName("total") val total: Int
)