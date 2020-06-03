package com.example.catapplication.network

import com.example.catapplication.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @POST("login")
    fun login(@Body body: Map<String, String>): Call<DataResponse>

    @POST("add-patient")
    @FormUrlEncoded
    fun addPatient(@FieldMap patient: HashMap<String, String>): Call<patientAddedResponse>


    @POST("patient/{patientId}/drop")
    @FormUrlEncoded
    fun dropPatient(@Path("patientId") patientId: Int, @FieldMap map: Map<String, String>): Call<DropResponse>

    @POST("patient/{patient}/switch")
    @FormUrlEncoded
    fun switchPatient(@Path("patient") patientId: Int, @FieldMap map: Map<String, String>): Call<ResponseBody>


    @GET("doctor-list/{userId}")
    fun getDoctorsList(@Path("userId") userId: Int): Call<DoctorsResponse>


    @GET("user/{userId}/logs")
    fun getPatientsByUserId(@Path("userId") userId: Int, @Query("page") page: Int): Call<PatientModelResponce>


    @GET("patient-list/{doctorId}")
    fun getPatientOfDoctorByPage(@Path("doctorId") doctorId: Int, @Query("page") page: Int): Call<PatientModelResponce>

    @GET("user-doctor-list/{userId}")
    fun getDoctorsByUser(@Path("userId") userId: Int): Call<UserDoctorsResponse>


    @GET("regions")
    fun getRegions(): Call<List<RegionResponse>>

    @GET("reasons")
    fun getReasones(): Call<ReasonsResponce>

    @GET("products")
    fun getCmlProducts(): Call<CmlResponce>

}