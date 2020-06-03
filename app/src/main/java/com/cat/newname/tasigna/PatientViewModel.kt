package com.example.catapplication.fragments.PatientsFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catapplication.models.DoctorsResponse
import com.example.catapplication.models.PatientModelResponce
import com.example.catapplication.models.UserDoctorsResponse
import com.example.catapplication.network.AddFragmentRepository
import okhttp3.ResponseBody

class PatientViewModel : ViewModel() {
    private var addFragmentRepository: AddFragmentRepository = AddFragmentRepository()

    /*fun getDoctorsList(userID: Int): MutableLiveData<UserDoctorsResponse> {
        return addFragmentRepository.getDoctorsListByUser(userID)
    }*/

    fun getDoctorsList(userID: Int): MutableLiveData<DoctorsResponse> {
        return addFragmentRepository.getDoctorsList(userID)
    }

    fun getPatientsList(userID: Int, pageId: Int): MutableLiveData<PatientModelResponce> {
        return addFragmentRepository.loadPatientsDataByUser(userID, pageId)
    }

    fun getPatientsListByDoctor(userID: Int, pageId: Int): MutableLiveData<PatientModelResponce> {
        return addFragmentRepository.loadPatientsDataOfDoctorByPage(userID, pageId)
    }
}