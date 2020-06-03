package com.example.catapplication.models

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody

/**
 * Common class used by API responses.
 * @param <T> the type of the responseBody
 **/
class ApiResponse {

    // if request throws exception and can't be send
    var exception: Throwable? = null

    // if request success and receive a response
    var isResponseSuccessful: Boolean = false // state of response
    var responseCode: Int? = 0 // response code

    // response body in case of response succeeded (T: the POJO class of the response)
    var responseBody: Map<String, Any>? = null

    // response body in case of response failed
    var errorMsg: String? = null

    var errorBody: ResponseBody? = null


    class Response(
        @SerializedName("data")
        val data: Any,
        @SerializedName("state")
        val state: String
    )
}