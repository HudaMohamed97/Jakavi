package com.example.catapplication.models

import com.google.gson.annotations.SerializedName



data class Doctors (
	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("kol_flag") val kol_flag : Int,
	@SerializedName("speciality") val speciality : String,
	@SerializedName("title") val title : String,
	@SerializedName("phone") val phone : String,
	@SerializedName("email") val email : String
)