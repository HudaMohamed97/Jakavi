package com.example.catapplication.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cat.newname.network.Webservice
import com.example.catapplication.models.DataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RepositoryHelper : RepositoryInterface {
    override fun login(email: String, password: String): MutableLiveData<DataResponse> {
        val userData = MutableLiveData<DataResponse>()
        val body = mapOf(
            "email" to email.trim(),
            "password" to password
        )
        Webservice.getInstance().api.login(body).enqueue(object : Callback<DataResponse> {
            override fun onResponse(call: Call<DataResponse>, response: Response<DataResponse>) {
                if (response.body()?.state == 1) {
                    userData.value = response.body()
                } else {
                    userData.value = response.body()
                    Log.i("hhhhhh", "error")
                }
            }

            override fun onFailure(call: Call<DataResponse>, t: Throwable) {
                userData.value = null
                Log.i("hhhhhh", "on fail")
                Log.i("hhhhhh", "on fail" + t.message)

            }
        })

        return userData

    }

    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}