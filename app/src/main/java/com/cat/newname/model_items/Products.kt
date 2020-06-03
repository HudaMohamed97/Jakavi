package com.example.catapplication.models

import com.google.gson.annotations.SerializedName

data class Products (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("category_id") val category_id : Int
)