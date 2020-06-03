package com.cat.newname.tasigna

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cat.newname.R
import com.example.catapplication.fragments.PatientsFragment.PatientViewModel
import com.example.catapplication.models.Doctors
import com.example.catapplication.models.DoctorsHospiatalResponce
import com.example.catapplication.models.PatientRepData
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import kotlinx.android.synthetic.main.tasigna_patient_fragment.*

class TasignaPatientsActivity : AppCompatActivity() {
    private lateinit var patientViewModel: PatientViewModel
    private lateinit var doctorSpinner: SearchableSpinner
    private lateinit var hospitalSpinner: SearchableSpinner
    private lateinit var recyclerView: RecyclerView
    private var selectedDoctorId = 0
    lateinit var shared: SharedPreferences
    private val allPatientsList = arrayListOf<PatientRepData>()
    private var onTouchListener: RecyclerTouchListener? = null
    private var touchListener: OnActivityTouchListener? = null
    private var dialog: ProgressDialog? = null
    private lateinit var adapter: Patient_adapter
    var mHasReachedBottomOnce = false
    var currentPageNum = 1
    var connecttionType = 1
    var lastPageNum: Int = 0
    private var selectedHospitalId = 0
    private val hospitalDoctorsList = arrayListOf<DoctorsHospiatalResponce>()
    var lastPageNumPatient: Int = 0
    private var storedSelctedDector = 0
    private lateinit var myDataHolder: SharedPreferences.Editor
    private lateinit var FromFragment: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasigna_patient_fragment)
        patientViewModel = ViewModelProviders.of(this).get(PatientViewModel::class.java)
        setClickListeners()
        FromFragment = intent.getStringExtra("fromFragment")!!
        shared = this.getSharedPreferences("id", Context.MODE_PRIVATE)
        initRecyclerView()
        callHospitalDoctorsList()
        callPatientsList(1, false)

    }

    private fun setClickListeners() {
        doctorSpinner = findViewById(R.id.spinnerDoctors)
        hospitalSpinner = findViewById(R.id.spinnerHospital)
        recyclerView = findViewById(R.id.patientsRecycler)
        doctorText.setOnClickListener {
            Toast.makeText(this, "Please Choose Hospital First", Toast.LENGTH_SHORT).show()
        }
        val button = findViewById(R.id.back) as ImageView
        button.setOnClickListener {
            onBackPressed()
        }
    }


    private fun callHospitalDoctorsList() {
        patientViewModel.getDoctorsList(getUserId()).observe(this, Observer {
            if (it != null) {
                for (hospitalName in it.data) {
                    hospitalDoctorsList.add(hospitalName)
                }
                prepareHospitalsList(hospitalDoctorsList)
            } else {
                Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT)
                        .show()
            }
        })
    }

    private fun prepareHospitalsList(hospitalDoctorsList: ArrayList<DoctorsHospiatalResponce>) {
        val hospitalNameList = arrayListOf<String>()
        hospitalNameList.add("All Patients")
        for (hospital in hospitalDoctorsList) {
            hospitalNameList.add(hospital.name)
        }
        initializeHospitalSpinner(hospitalSpinner, hospitalNameList)
    }


    private fun callPatientsList(pageId: Int, fromLoadMore: Boolean) {
        if (fromLoadMore) {
            loading.visibility = View.VISIBLE
        } else {
            showLoader()
        }
        patientViewModel.getPatientsList(getUserId(), pageId).observe(this, Observer {
            nopatienttext.visibility = View.GONE
            if (fromLoadMore) {
                loading.visibility = View.GONE
            } else {
                hideLoader()
            }
            if (it != null) {
                if (!fromLoadMore) {
                    allPatientsList.clear()
                }
                lastPageNum = it.patientData.last_page
                for (data in it.patientData.data) {
                    allPatientsList.add(data)
                }
                adapter.notifyDataSetChanged()
                mHasReachedBottomOnce = false
                currentPageNum++
            } else {
                hideLoader()
                Toast.makeText(
                        this,
                        " there is an Error Occurred",
                        Toast.LENGTH_SHORT
                )
                        .show()
            }

        })
    }

    private fun callPatientsListByDoctor(selectedDoctorId: Int, pageId: Int, loadMore: Boolean) {
        if (loadMore) {
            loading.visibility = View.VISIBLE
        } else {
            showLoader()
        }
        patientViewModel.getPatientsListByDoctor(selectedDoctorId, pageId).observe(this, Observer {
            if (loadMore) {
                loading.visibility = View.GONE
            } else {
                hideLoader()
            }
            if (it != null) {
                if (!loadMore) {
                    allPatientsList.clear()
                }
                if (it.state == 1) {
                    nopatienttext.visibility = View.GONE
                    lastPageNumPatient = it.patientData.last_page
                    for (data in it.patientData.data) {
                        allPatientsList.add(data)
                    }
                    adapter.notifyDataSetChanged()
                    mHasReachedBottomOnce = false
                    currentPageNum++
                } else {
                    adapter.notifyDataSetChanged()
                    mHasReachedBottomOnce = false
                    currentPageNum++
                    if (loadMore) {
                        nopatienttext.visibility = View.GONE
                        Toast.makeText(
                                this,
                                " No More Patients for the Doctor",
                                Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        nopatienttext.visibility = View.VISIBLE
                        Toast.makeText(
                                this,
                                " No Patients for the Doctor",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } else {
                nopatienttext.visibility = View.GONE
                hideLoader()
                Toast.makeText(
                        this,
                        " there is an Network Error",
                        Toast.LENGTH_SHORT
                )
                        .show()
            }
        })
    }


    private fun hideLoader() {
        dialog?.dismiss()

    }

    private fun showLoader() {
        dialog = ProgressDialog(this)
        dialog?.setMessage("Please, Wait")
        dialog?.setCancelable(false)
        dialog?.show()
    }


    private fun prepareDoctorsList(doctors: ArrayList<Doctors>) {
        val doctorsNameList = arrayListOf<String>()
        var doctorsList = arrayListOf<Doctors>()
        for (doctor in doctors) {
            doctorsList.add(doctor)
            doctorsNameList.add(doctor.name)
        }
        initializeSpinner(doctorSpinner, doctorsNameList, doctorsList)
    }

    private fun getUserId(): Int {
        Log.i("hhh", "" + shared.getInt("id", 0))
        return shared.getInt("id", 0)
    }


    private fun initializeHospitalSpinner(
            spinner: SearchableSpinner,
            hospitalNameList: ArrayList<String>
    ) {
        val arrayAdapter =
                this?.let {
                    ArrayAdapter(it, R.layout.spinner_item, hospitalNameList)

                }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
            ) {
                if (position == 0) {
                    doctorText.visibility = View.VISIBLE
                    doctorSpinner.visibility = View.GONE
                    currentPageNum = 1
                    callPatientsList(currentPageNum, false)
                    connecttionType = 1
                    storedSelctedDector = 0
                } else {
                    selectedHospitalId = hospitalDoctorsList[position - 1].id
                    prepareDoctorsList(hospitalDoctorsList[position - 1].doctors as ArrayList<Doctors>)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }
        }
        arrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        if (arrayAdapter != null) {
            spinner.adapter = arrayAdapter
        }
    }


    private fun initializeSpinner(
            spinner: SearchableSpinner,
            doctorsNameList: ArrayList<String>,
            doctorsList: ArrayList<Doctors>
    ) {
        val arrayAdapter =
                this?.let { ArrayAdapter(it, R.layout.spinner_item, doctorsNameList) }
        doctorText.visibility = View.GONE
        doctorSpinner.visibility = View.VISIBLE
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
            ) {
                storedSelctedDector = position
                connecttionType = 2
                currentPageNum = 1
                selectedDoctorId = doctorsList[position].id
                callPatientsListByDoctor(selectedDoctorId, currentPageNum, false)
                myDataHolder = shared.edit()
                myDataHolder.putInt("storedSelctedDector", storedSelctedDector)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }


        }
        arrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        if (arrayAdapter != null) {
            spinner.adapter = arrayAdapter
        }
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = Patient_adapter(this, allPatientsList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        adapter.setOnItemListener(object : Patient_adapter.OnItemClickListener {
            override fun onItemClicked(position: Int) {
                val patientId = allPatientsList[position].id
                if (FromFragment == "fromSwitch") {
                    var bundle = bundleOf("fromFragment" to "fromSwitch", "patientId" to patientId)
                    /*findNavController().navigate(
                            R.id.action_PatientFragment_to_SwitchFragment,
                            bundle
                    )*/
                } else {
                    var bundle = bundleOf("fromFragment" to "fromDrop", "patientId" to patientId)
                    /* findNavController().navigate(
                             R.id.action_PatientFragment_to_DropFragment,
                             bundle
                     )*/
                }
            }

        })
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && !mHasReachedBottomOnce) {
                    mHasReachedBottomOnce = true
                    if (currentPageNum <= lastPageNum) {
                        if (connecttionType == 1) {
                            if (currentPageNum <= lastPageNum)
                                callPatientsList(currentPageNum, true)
                        } else if (connecttionType == 2) {
                            if (currentPageNum <= lastPageNum)
                                callPatientsListByDoctor(selectedDoctorId, currentPageNum, true)
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        onTouchListener?.let { recyclerView.addOnItemTouchListener(it) }
    }

}