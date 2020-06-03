package com.example.catapplication.models

import com.google.gson.annotations.SerializedName

data class PatientData(
    @SerializedName("user_id ")
    val user_id: Int,
    @SerializedName("doctor_id ")
    val doctor_id: Int,
    @SerializedName("region_id ")
    val region_id: Int,
    @SerializedName("city_id ")
    val city_id: Int,
    @SerializedName("category_id ")
    val category_id: Int,
    @SerializedName("product_id ")
    val product_id: Int,
    @SerializedName("hospital_id ")
    val hospital_id: Int
)
