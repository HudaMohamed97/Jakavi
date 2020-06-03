package com.example.catapplication.models

import com.google.gson.annotations.SerializedName


data class CmlData(

    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("products") val products: List<Products>
)