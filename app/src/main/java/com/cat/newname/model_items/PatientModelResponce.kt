package com.example.catapplication.models

import com.google.gson.annotations.SerializedName

data class PatientModelResponce(
    @SerializedName("patientData") val patientData: AllPatientModel,
    @SerializedName("state") val state: Int
)