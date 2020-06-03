package com.cat.newname.tasigna

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.cat.newname.R
import com.example.catapplication.models.*
import com.toptoche.searchablespinnerlibrary.SearchableSpinner
import kotlinx.android.synthetic.main.tasigna_add_user_fragment.*
import kotlinx.android.synthetic.main.tasigna_home_fragment.*

class TasignaAddPatient : AppCompatActivity() {
    private lateinit var spinner: SearchableSpinner
    private lateinit var mainLayout: View
    private lateinit var spinnerDose: SearchableSpinner
    private lateinit var spinnerCity: SearchableSpinner
    private lateinit var spinnerRegion: SearchableSpinner
    private lateinit var spinnerCml: SearchableSpinner
    private lateinit var doctorsSpinner: SearchableSpinner
    private lateinit var spinnersDropReasons: SearchableSpinner
    private lateinit var noteTitle: TextView
    private lateinit var title: TextView
    private lateinit var noteText: EditText
    private lateinit var addButton: ImageView
    private lateinit var back: ImageView
    private var selectedHospitalId = 0
    private var selectedDoctorId = 0
    private var selectedReasonId = 0
    private var selectedRegionId = 0
    private var selectedCmlId = 0
    private var selectedDoseId = 0
    private var selectedSwitchtoId = 0
    private var selectedCityId = 0
    lateinit var shared: SharedPreferences
    private lateinit var addFragmentViewModel: AddActivityViewModel
    private val hospitalDoctorsList = arrayListOf<DoctorsHospiatalResponce>()
    private val ReasonsList = arrayListOf<ReasonData>()
    private val regionList = arrayListOf<RegionResponse>()
    private val cmlList = arrayListOf<CmlData>()
    private val hospitalNameList = arrayListOf<String>()
    private val ReasonsNameList = arrayListOf<String>()
    private val regionNameList = arrayListOf<String>()
    private val cmlNameList = arrayListOf<String>()
    private lateinit var FromFragment: String
    private var PatientId = 0
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasigna_add_user_fragment)
        back.setOnClickListener { onBackPressed() }
        FromFragment = intent?.getStringExtra("fromFragment").toString()
        PatientId = intent?.getIntExtra("patientId", 0)!!
        setViews()
        setListener()
        if (FromFragment == "fromAdd") {
            getRegionsList()
            callDoctorsList()
            getCmlList()
            addButton.setImageResource(R.drawable.addpatient)
            title.text = "Add Patient"

        }
        if (FromFragment == "fromDrop") {
            doctorsSpinner.visibility = View.GONE
            spinnerCity.visibility = View.GONE
            spinnerRegion.visibility = View.GONE
            doseText.visibility = View.GONE
            cityText.visibility = View.GONE
            doctorText.visibility = View.GONE
            spinnerDose.visibility = View.GONE
            spinnerCml.visibility = View.GONE
            spinner.visibility = View.GONE
            spinnersDropReasons.visibility = View.VISIBLE
            noteTitle.visibility = View.VISIBLE
            noteText.visibility = View.VISIBLE
            addButton.setImageResource(R.drawable.droppatient)
            title.text = "Drop Patient"
            callReasonsList()
        }
        if (FromFragment == "fromSwitch") {
            addButton.setImageResource(R.drawable.switchpatient)
            doseText.visibility = View.GONE
            cityText.visibility = View.GONE
            doctorText.visibility = View.GONE
            doctorsSpinner.visibility = View.GONE
            spinnerCity.visibility = View.GONE
            spinnerRegion.visibility = View.GONE
            spinnerDose.visibility = View.VISIBLE
            spinnerCml.visibility = View.VISIBLE
            spinner.visibility = View.GONE
            spinnersDropReasons.visibility = View.GONE
            title.text = "Switch Patient"
            getCmlList()
        }
    }

    private fun setListener() {
        mainLayout.setOnClickListener {
            hideKeyboard()
        }
        addButton.setOnClickListener {
            hideKeyboard()
            if (FromFragment == "fromAdd") {
                if (selectedHospitalId == 0 || selectedDoctorId == 0 || selectedRegionId == 0 || selectedCityId == 0 || selectedCmlId == 0 || selectedDoseId == 0) {
                    Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT)
                            .show()
                } else {
                    callAddPatient()
                }
            }
            if (FromFragment == "fromDrop") {
                if (selectedReasonId == 0) {
                    Toast.makeText(this, "Please choose Reason", Toast.LENGTH_SHORT)
                            .show()
                } else {
                    Log.i("hhh", "" + PatientId)
                    val map = HashMap<String, String>()
                    map["user_id"] = getUserId().toString()
                    map["reason_id"] = selectedReasonId.toString()
                    map["notes"] = noteText.text.toString()
                    showLoader()
                    addFragmentViewModel.dropPatient(PatientId, map).observe(this, Observer {
                        hideLoader()
                        if (it == null) {
                            Toast.makeText(
                                    this,
                                    "Patient Dropped faild",
                                    Toast.LENGTH_SHORT
                            )
                                    .show()
                            openAlertDialog("Patient Dropped faild")
                        } else {
                            hideLoader()
                            if (it.state == 1) {
                                Toast.makeText(
                                        this,
                                        "Patient Dropped Successfully",
                                        Toast.LENGTH_SHORT
                                )
                                        .show()
                                openAlertDialog("Patient Dropped Successfully")
                            } else {
                                Toast.makeText(
                                        this,
                                        "Patient Dropped faild",
                                        Toast.LENGTH_SHORT
                                )
                                        .show()
                                openAlertDialog("Patient Dropped failed")
                            }
                        }

                    })
                }
            }
            if (FromFragment == "fromSwitch") {
                if (selectedCmlId == 0 || selectedDoseId == 0) {
                    Toast.makeText(this, "Please choose cml and dose", Toast.LENGTH_SHORT)
                            .show()
                } else {
                    val map = HashMap<String, String>()
                    map["user_id"] = getUserId().toString()
                    map["category_id"] = selectedCmlId.toString()
                    map["product_id"] = selectedDoseId.toString()
                    showLoader()
                    addFragmentViewModel.switchPatient(PatientId, map).observe(this, Observer {
                        hideLoader()
                        if (it == null) {
                            Toast.makeText(
                                    this,
                                    "Patient Switched faild",
                                    Toast.LENGTH_SHORT
                            )
                                    .show()
                            openAlertDialog("Patient Switched faild")
                        } else {
                            hideLoader()
                            openAlertDialog("Patient Switched Successfully")
                            Toast.makeText(
                                    this,
                                    "Patient Switched Successfully",
                                    Toast.LENGTH_SHORT
                            )
                                    .show()

                        }

                    })
                }

            }
        }

    }


    private fun listenToLoading() {
        addFragmentViewModel.isLoading.observe(this, Observer {
            if (it) {
                showLoader()
            } else {
                hideLoader()
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


    private fun callAddPatient() {
        val map = HashMap<String, String>()
        map["user_id"] = getUserId().toString()
        map["doctor_id"] = selectedDoctorId.toString()
        map["region_id"] = selectedRegionId.toString()
        map["city_id"] = selectedCityId.toString()
        map["category_id"] = selectedCmlId.toString()
        map["product_id"] = selectedDoseId.toString()
        map["hospital_id"] = selectedHospitalId.toString()

        addFragmentViewModel.addPatient(map).observe(this, Observer {
            if (it.state == 1) {
                Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
            } else {
                openAlertDialog("Patient Not Updated Please Try Again, Thanks")
                Toast.makeText(
                        this,
                        "Patient Not Updated Please Try Again, Thanks",
                        Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun setViews() {
        spinner = findViewById(R.id.spinnerHospital)
        doctorsSpinner = findViewById(R.id.spinnerDoctorName)
        doctorText.setOnClickListener {
            Toast.makeText(
                    this,
                    "Please Choose Hospital First",
                    Toast.LENGTH_SHORT
            ).show()
        }
        //  initializeDoctorsSpinner(doctorsSpinner, ArrayList(), ArrayList())
        spinnerRegion = findViewById(R.id.spinnerRegion)
        spinnerCity = findViewById(R.id.spinnerCity)
        cityText.setOnClickListener {
            Toast.makeText(
                    this,
                    "Please Choose Region First",
                    Toast.LENGTH_SHORT
            ).show()
        }
        spinnerCml = findViewById(R.id.spinnerCml)
        spinnerDose = findViewById(R.id.spinnerDose)
        doseText.setOnClickListener {
            Toast.makeText(
                    this,
                    "Please Choose CML First",
                    Toast.LENGTH_SHORT
            ).show()
        }

        addButton = findViewById(R.id.btn_Add)
        spinnersDropReasons = findViewById(R.id.spinnersDropReasons)
        noteTitle = findViewById(R.id.noteText)
        noteText = findViewById(R.id.result)
        title = findViewById(R.id.title)
        back = findViewById(R.id.back)
        mainLayout = findViewById(R.id.mainLayout) as View
    }

    private fun initializeSpinner(spinner: SearchableSpinner, hospitalNameList: ArrayList<String>) {
        val arrayAdapter =
                this.let { ArrayAdapter(it, R.layout.spinner_item, hospitalNameList) }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
            ) {
                selectedHospitalId = hospitalDoctorsList[position].id
                prepareDoctorsList(hospitalDoctorsList[position].doctors as ArrayList<Doctors>)
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


    private fun initializeRegionSpinner(
            spinner: SearchableSpinner,
            regionNameList: ArrayList<String>
    ) {
        val arrayAdapter =
                this.let { ArrayAdapter(it, R.layout.spinner_item, regionNameList) }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
            ) {

                val region = parentView.getItemAtPosition(position).toString()
                selectedRegionId = regionList[position].id
                prepareCitiesList(regionList[position].cities as ArrayList<Cities>)
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


    private fun initializeCmlSpinner(
            spinner: SearchableSpinner,
            cmlNameList: ArrayList<String>
    ) {
        val arrayAdapter =
                this.let { ArrayAdapter(it, R.layout.spinner_item, cmlNameList) }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
            ) {

                val cml = parentView.getItemAtPosition(position).toString()
                selectedCmlId = cmlList[position].id
                prepareDoseList(cmlList[position].products as ArrayList<Products>)
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


    private fun initializeDoctorsSpinner(
            spinner: SearchableSpinner,
            hospitalNameList: ArrayList<String>
            , hospitalsDoctorsList: ArrayList<Doctors>
    ) {
        doctorText.visibility = View.GONE
        doctorsSpinner.visibility = View.VISIBLE
        val arrayAdapter =
                this?.let { ArrayAdapter(it, R.layout.spinner_item, hospitalNameList) }

        if (arrayAdapter != null) {
            spinner.adapter = arrayAdapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
            ) {
                parentView.getItemAtPosition(position).toString()
                selectedDoctorId = hospitalsDoctorsList[position].id
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }

        arrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


    }

    private fun initializeSpinnersDropReasons(
            spinner: SearchableSpinner,
            ReasonsNameList: ArrayList<String>
    ) {
        val arrayAdapter =
                this.let { ArrayAdapter(it, R.layout.spinner_item, ReasonsNameList) }

        if (arrayAdapter != null) {
            spinner.adapter = arrayAdapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
            ) {
                selectedReasonId = ReasonsList[position].id
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // your code here
            }

        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }


    private fun initializeCitySpinner(
            spinner: SearchableSpinner,
            cityNameList: ArrayList<String>
            , cityList: ArrayList<Cities>
    ) {
        cityText.visibility = View.GONE
        spinnerCity.visibility = View.VISIBLE

        val arrayAdapter =
                this.let { ArrayAdapter(it, R.layout.spinner_item, cityNameList) }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
            ) {
                val city = parentView.getItemAtPosition(position).toString()
                selectedCityId = cityList[position].id
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

    private fun initializDoseSpinner(
            spinner: SearchableSpinner,
            doseNameList: ArrayList<String>, doseList: ArrayList<Products>
    ) {

        doseText.visibility = View.GONE
        spinnerDose.visibility = View.VISIBLE

        val arrayAdapter =
                this?.let { ArrayAdapter(it, R.layout.spinner_item, doseNameList) }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
            ) {

                val dose = parentView.getItemAtPosition(position).toString()
                selectedDoseId = doseList[position].id
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

    private fun initializSwitchSpinner(
            spinner: SearchableSpinner,
            doseNameList: ArrayList<String>, doseList: ArrayList<Products>
    ) {
        val arrayAdapter =
                this.let { ArrayAdapter(it, R.layout.spinner_item, doseNameList) }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    parentView: AdapterView<*>,
                    selectedItemView: View,
                    position: Int,
                    id: Long
            ) {

                val dose = parentView.getItemAtPosition(position).toString()
                selectedSwitchtoId = doseList[position].id
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


    private fun callDoctorsList() {
        addFragmentViewModel.getDoctorsList(getUserId()).observe(this, Observer {
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

    private fun callReasonsList() {
        addFragmentViewModel.getReasonsList().observe(this, Observer {
            if (it != null) {
                for (reason in it.data) {
                    ReasonsList.add(reason)
                }
                prepareReasonsList(ReasonsList)
            }
        })
    }

    private fun getRegionsList() {
        showLoader()
        addFragmentViewModel.getRegionsList().observe(this, Observer {
            if (it != null) {
                hideLoader()
                for (region in it) {
                    regionList.add(region)
                }
                prepareRegionsList(regionList)
            } else {
                hideLoader()
                Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT)
                        .show()
            }
        })
    }

    private fun getCmlList() {
        addFragmentViewModel.getCmlList().observe(this, Observer {
            if (it != null) {
                for (cml in it.data) {
                    cmlList.add(cml)
                }
                prepareCmlList(cmlList)
            }
        })
    }

    private fun prepareHospitalsList(hospitalDoctorsList: ArrayList<DoctorsHospiatalResponce>) {
        for (hospital in hospitalDoctorsList) {
            hospitalNameList.add(hospital.name)
        }
        initializeSpinner(spinner, hospitalNameList)


    }

    private fun prepareReasonsList(hospitalDoctorsList: ArrayList<ReasonData>) {
        for (reason in hospitalDoctorsList) {
            ReasonsNameList.add(reason.name)
        }
        initializeSpinnersDropReasons(spinnersDropReasons, ReasonsNameList)


    }

    private fun prepareRegionsList(regionList: ArrayList<RegionResponse>) {
        for (region in regionList) {
            regionNameList.add(region.name)
        }
        initializeRegionSpinner(spinnerRegion, regionNameList)

    }

    private fun prepareCmlList(CmlList: ArrayList<CmlData>) {
        for (cml in CmlList) {
            cmlNameList.add(cml.name)
        }
        initializeCmlSpinner(spinnerCml, cmlNameList)

    }

    private fun prepareDoctorsList(hospitalsDoctorsList: ArrayList<Doctors>) {
        val doctorsNameList = arrayListOf<String>()
        for (doctor in hospitalsDoctorsList) {
            doctorsNameList.add(doctor.name)
        }
        initializeDoctorsSpinner(doctorsSpinner, doctorsNameList, hospitalsDoctorsList)
    }

    private fun prepareCitiesList(cityList: ArrayList<Cities>) {
        val citiesList = arrayListOf<String>()
        for (city in cityList) {
            citiesList.add(city.name)
        }
        initializeCitySpinner(spinnerCity, citiesList, cityList)
    }

    private fun prepareDoseList(doseList: ArrayList<Products>) {
        val list = arrayListOf<String>()
        for (dose in doseList) {
            list.add(dose.name)
        }
        initializDoseSpinner(spinnerDose, list, doseList)
    }


    private fun getUserId(): Int {
        shared = this.getSharedPreferences("id", Context.MODE_PRIVATE)
        return shared.getInt("id", 0)
    }


    private fun openAlertDialog(massage: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(massage)
        builder1.setCancelable(true)
        builder1.setPositiveButton(
                "ok"
        ) { dialog, id -> dialog.cancel() }
        val alert = builder1.create()
        alert.show()


    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}