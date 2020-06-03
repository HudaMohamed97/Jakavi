package com.example.catapplication.models

import com.google.gson.annotations.SerializedName

data class patientAddedResponse(

    @SerializedName("msg") val msg: String,
    @SerializedName("data") val data: PatientData,
    @SerializedName("state") val state: Int
)