package com.example.catapplication.network

import androidx.lifecycle.MutableLiveData
import com.example.catapplication.models.Data
import com.example.catapplication.models.DataResponse

interface RepositoryInterface {

    fun login(email: String, Password: String): MutableLiveData<DataResponse>

    fun update()
}