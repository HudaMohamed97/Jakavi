package com.example.catapplication.models

import Pivot
import com.google.gson.annotations.SerializedName

data class DoctorsHospiatalResponce(

    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("region_id") val region_id: Int,
    @SerializedName("city_id") val city_id: Int,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("doctors") val doctors: List<Doctors>,
    @SerializedName("pivot") val pivot: Pivot
)