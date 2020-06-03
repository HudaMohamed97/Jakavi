package com.cat.newname.network_interface;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ServiceInterface {

    @POST("login")
    @FormUrlEncoded
    Call<ResponseBody> login(@FieldMap Map<String, String> map);


    @GET("user/{userId}/logs")
    Call<ResponseBody> getPatients(@Path("userId") int userId);


    @GET("user/{userId}/logs")
    Call<ResponseBody> getPatientsByPage(@Path("userId") int userId, @Query("page") int page);


    @GET("user-doctor-list/{userId}")
    Call<ResponseBody> getDoctors(@Path("userId") int userId);


    @GET("patient-list/{doctorId}")
    Call<ResponseBody> getPatientOfDoctor(@Path("doctorId") int doctorId);


    @GET("patient-list/{doctorId}")
    Call<ResponseBody> getPatientOfDoctorByPage(@Path("doctorId") int doctorId, @Query("page") int page);


    @POST("add-patient")
    @FormUrlEncoded
    Call<ResponseBody> addPatient(@FieldMap Map<String, String> map);


    @POST("patient/{patientId}/delete")
    @FormUrlEncoded
    Call<ResponseBody> deletePatient(@Path("patientId") int doctorId, @FieldMap Map<String, String> map);


    @GET("product/{productId}/doses")
    Call<ResponseBody> getDoses(@Path("productId") String productId);


    @POST("patient/{patientId}/update")
    @FormUrlEncoded
    Call<ResponseBody> updatePatientDose(@Path("patientId") String patientId, @FieldMap Map<String, String> map);

    @GET("doctor-list/{userId}")
    Call<ResponseBody> getHospitalAndDoctors(@Path("userId") int userId);


    @GET("products")
    Call<ResponseBody> getProducts();

}
