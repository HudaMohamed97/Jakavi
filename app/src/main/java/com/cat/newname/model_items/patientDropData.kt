package com.example.catapplication.models

import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2020 Kotlin com.example.catapplication.models.Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class patientDropData (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("username") val username : String,
	@SerializedName("email") val email : String,
	@SerializedName("phone") val phone : Int,
	@SerializedName("address") val address : String,
	@SerializedName("player_id") val player_id : String,
	@SerializedName("created_at") val created_at : String,
	@SerializedName("account_type") val account_type : String,
	@SerializedName("flm_manager") val flm_manager : String,
	@SerializedName("region_name") val region_name : String,
	@SerializedName("city_name") val city_name : String,
	@SerializedName("target") val target : String,
	@SerializedName("score") val score : Int
)