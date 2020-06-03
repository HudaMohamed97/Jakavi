package com.example.catapplication.models

import com.google.gson.annotations.SerializedName

data class DoctorsResponse (

	@SerializedName("data") val data : List<DoctorsHospiatalResponce>,
	@SerializedName("state") val state : Int
)