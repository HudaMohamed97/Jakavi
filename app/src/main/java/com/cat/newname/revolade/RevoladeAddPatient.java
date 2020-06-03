package com.cat.newname.revolade;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.newname.R;
import com.cat.newname.network_interface.ServiceInterface;
import com.cat.newname.model_items.Product_item;
import com.cat.newname.model_items.Doctor_item;
import com.cat.newname.model_items.Dose_item;
import com.cat.newname.model_items.Hospital_item;
import com.cat.newname.model_items.Category_item;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RevoladeAddPatient extends AppCompatActivity {

    ImageView allpatients, back, addPatient;

    String url;
    int userId;
    Retrofit retrofit;
    ServiceInterface myInterface;
    private ProgressDialog dialog;

    ArrayList<Hospital_item> hospitals = new ArrayList<>();
    ArrayList<Doctor_item> doctors = new ArrayList<>();
    ArrayList<Category_item> categories = new ArrayList<>();
    ArrayList<Product_item> products = new ArrayList<>();
    ArrayList<Dose_item> doses = new ArrayList<>();


    SharedPreferences shared;

    TextView hospital, doctor, product, category, dose;
    int selectedHospitalId = 0, selectedDoctorId = 0, selectedProductId = 0, selectedCategoryId = 0, selectedDoseId = 0;
    SpinnerDialog hospitalsSpinner, doctorsSpinner, productsSpinner, categoriesSpinner, dosesSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revolade_add_patient);


        shared = getSharedPreferences("id", Context.MODE_PRIVATE);
        userId = shared.getInt("id", 0);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait");
        dialog.setCancelable(false);


        url = getResources().getString(R.string.services_url);

        retrofit = new Retrofit.Builder()
                .baseUrl(url).
                        addConverterFactory(GsonConverterFactory.create()).build();
        myInterface = retrofit.create(ServiceInterface.class);


        loadHospitalsWithDoctorsData();
        loadProductsData();


        back = findViewById(R.id.back);
        allpatients = findViewById(R.id.allpatients);
        addPatient = findViewById(R.id.addPatient);

        hospital = findViewById(R.id.hospital);
        doctor = findViewById(R.id.doctor);
        product = findViewById(R.id.product);
        category = findViewById(R.id.category);
        dose = findViewById(R.id.dose);


        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hospital.getText().equals("Hospital") || doctor.getText().equals("Doctor") || category.getText().equals("Category") || product.getText().equals("Product")) {
                    Toast.makeText(RevoladeAddPatient.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                } else {
                    addPatient();
                }

            }
        });


        allpatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), RevoladeAllPatients.class);
                startActivity(i);
            }
        });

        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initHospitalssSpinner();
                hospitalsSpinner.showSpinerDialog();
            }
        });


        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hospital.getText().equals("Hospital")) {
                    Toast.makeText(RevoladeAddPatient.this, "Please Choose Hospital first", Toast.LENGTH_SHORT).show();
                } else {
                    doctorsSpinner.showSpinerDialog();
                }
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initCategoriesSpinner();
                categoriesSpinner.showSpinerDialog();
            }
        });


        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (category.getText().equals("Category")) {
                    Toast.makeText(RevoladeAddPatient.this, "Please Choose Category first", Toast.LENGTH_SHORT).show();
                } else {
                    productsSpinner.showSpinerDialog();
                }
            }
        });


        dose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getText().equals("Product")) {
                    Toast.makeText(RevoladeAddPatient.this, "Please Choose Product first", Toast.LENGTH_SHORT).show();
                } else {
                    dosesSpinner.showSpinerDialog();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }


    public void addPatient() {

        dialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));
        map.put("category_id", String.valueOf(selectedCategoryId));
        map.put("product_id", String.valueOf(selectedProductId));
        map.put("doctor_id", String.valueOf(selectedDoctorId));

        if (dose.getText().equals("Dose")) {
            map.put("dose_id", " ");
        } else if (dose.getText().equals("Dose")) {
            map.put("dose_id", String.valueOf(selectedDoseId));
        }


        Log.d("Add Patient Map : ", map.toString());


        myInterface.addPatient(map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    String msg = responseObject.getString("msg");

                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();

                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }


    public void loadHospitalsWithDoctorsData() {

        dialog.show();
        myInterface.getHospitalAndDoctors(userId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONArray hospitalswithDoctorsArray = responseObject.getJSONArray("data");

                    setHospitalsList(hospitalswithDoctorsArray);


                    Log.d("Response", hospitalswithDoctorsArray.toString());

                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error Throw", t.toString());
                Log.d("commit Test Throw", t.toString());
                Log.d("Call", t.toString());
                Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    public void setHospitalsList(JSONArray list) {

        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = currentobject.getInt("id");
                final String name = currentobject.getString("name");
                JSONArray doctorsArray = currentobject.getJSONArray("doctors");
                hospitals.add(new Hospital_item(id, name, setDoctorsList(doctorsArray)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Doctor_item> setDoctorsList(JSONArray list) {

        ArrayList<Doctor_item> filterList = new ArrayList<>();

        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = currentobject.getInt("id");
                final String name = currentobject.getString("name");
                filterList.add(new Doctor_item(id, name));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filterList;

    }


    public void loadProductsData() {

//        dialog.show();
        myInterface.getProducts().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONArray categoriessArray = responseObject.getJSONArray("data");

                    setCategoriesList(categoriessArray);


                    Log.d("Response", categoriessArray.toString());

//                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error Throw", t.toString());
                Log.d("commit Test Throw", t.toString());
                Log.d("Call", t.toString());
                Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
            }
        });
    }


    public void setCategoriesList(JSONArray list) {

        try {

//            for (int i = 0; i < list.length(); i++) {
            JSONObject currentobject = list.getJSONObject(2);
            final int id = currentobject.getInt("id");
            final String name = currentobject.getString("name");
            JSONArray productsArray = currentobject.getJSONArray("products");
            categories.add(new Category_item(id, name, setProductssList(productsArray)));

//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public ArrayList<Product_item> setProductssList(JSONArray list) {

        ArrayList<Product_item> filterList = new ArrayList<>();

        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = currentobject.getInt("id");
                final String name = currentobject.getString("name");
                JSONArray dosesArray = currentobject.getJSONArray("doses");
                filterList.add(new Product_item(id, name, setDosesList(dosesArray)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filterList;

    }


    public ArrayList<Dose_item> setDosesList(JSONArray list) {

        ArrayList<Dose_item> filterList = new ArrayList<>();

        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = currentobject.getInt("id");
                final String name = currentobject.getString("name");
                filterList.add(new Dose_item(id, name));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filterList;

    }


    private void initHospitalssSpinner() {

        hospitalsSpinner = new SpinnerDialog(RevoladeAddPatient.this, prepareHospitalsStringList(hospitals), "Select Hospital", "");// With No Animation

        hospitalsSpinner.setCancellable(true); // for cancellable
        hospitalsSpinner.setShowKeyboard(false);// for open keyboard by default


        hospitalsSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                hospital.setText(hospitals.get(position).getName());
                selectedHospitalId = hospitals.get(position).getId();

                doctors = hospitals.get(position).getDoctors();
                doctor.setText("Doctor");
                selectedDoctorId = 0;

                initDoctorsSpinner(prepareDoctorsStringList(hospitals.get(position).getDoctors()));

            }
        });

    }


    public ArrayList<String> prepareHospitalsStringList(ArrayList<Hospital_item> list) {

        ArrayList<String> stringList = new ArrayList<>();

        try {

            for (int i = 0; i < list.size(); i++) {
                stringList.add(list.get(i).getName());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringList;

    }


    private void initDoctorsSpinner(ArrayList<String> list) {

        doctorsSpinner = new SpinnerDialog(RevoladeAddPatient.this, list, "Select Doctor", "");// With No Animation

        doctorsSpinner.setCancellable(true); // for cancellable
        doctorsSpinner.setShowKeyboard(false);// for open keyboard by default


        doctorsSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                doctor.setText(doctors.get(position).getName());
                selectedDoctorId = doctors.get(position).getId();


            }
        });

    }


    public ArrayList<String> prepareDoctorsStringList(ArrayList<Doctor_item> list) {

        ArrayList<String> stringList = new ArrayList<>();

        try {

            for (int i = 0; i < list.size(); i++) {
                stringList.add(list.get(i).getName());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringList;

    }


    private void initCategoriesSpinner() {

        categoriesSpinner = new SpinnerDialog(RevoladeAddPatient.this, prepareCategoriesStringList(categories), "Select Category", "");// With No Animation

        categoriesSpinner.setCancellable(true); // for cancellable
        categoriesSpinner.setShowKeyboard(false);// for open keyboard by default


        categoriesSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                category.setText(item);
                selectedCategoryId = categories.get(position).getId();

                products = categories.get(position).getProducts();

                product.setText("Product");
                selectedProductId = 0;

                dose.setText("Dose");
                selectedDoseId = 0;
                initProductsSpinner(prepareProductsStringList(categories.get(position).getProducts()));

            }
        });

    }


    public ArrayList<String> prepareCategoriesStringList(ArrayList<Category_item> list) {

        ArrayList<String> stringList = new ArrayList<>();

        try {

            for (int i = 0; i < list.size(); i++) {
                stringList.add(list.get(i).getName());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringList;

    }


    private void initProductsSpinner(ArrayList<String> list) {

        productsSpinner = new SpinnerDialog(RevoladeAddPatient.this, prepareProductsStringList(products), "Select Product", "");// With No Animation

        productsSpinner.setCancellable(true); // for cancellable
        productsSpinner.setShowKeyboard(false);// for open keyboard by default


        productsSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                product.setText(item);
                selectedProductId = products.get(position).getId();

                doses = products.get(position).getDoses();
                initDosesSpinner(prepareDosesStringList(products.get(position).getDoses()));


                dose.setText("Dose");
                selectedDoseId = 0;

            }
        });

    }


    public ArrayList<String> prepareProductsStringList(ArrayList<Product_item> list) {

        ArrayList<String> stringList = new ArrayList<>();

        try {

            for (int i = 0; i < list.size(); i++) {
                stringList.add(list.get(i).getName());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringList;

    }


    private void initDosesSpinner(ArrayList<String> list) {

        if (list.isEmpty()) {
            dose.setVisibility(View.GONE);
        } else {
            dose.setVisibility(View.VISIBLE);
        }

        dosesSpinner = new SpinnerDialog(RevoladeAddPatient.this, list, "Select Dose", "");// With No Animation

        dosesSpinner.setCancellable(true); // for cancellable
        dosesSpinner.setShowKeyboard(false);// for open keyboard by default


        dosesSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                dose.setText(item);
                selectedDoseId = doses.get(position).getId();

            }
        });

    }


    public ArrayList<String> prepareDosesStringList(ArrayList<Dose_item> list) {

        ArrayList<String> stringList = new ArrayList<>();

        try {

            for (int i = 0; i < list.size(); i++) {
                stringList.add(list.get(i).getName());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringList;

    }


}
