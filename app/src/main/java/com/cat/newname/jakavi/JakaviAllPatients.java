package com.cat.newname.jakavi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cat.newname.R;
import com.cat.newname.network_interface.ServiceInterface;
import com.cat.newname.adapters.Patient_adapter;
import com.cat.newname.dialogs.JakaviEditDoseDialog;
import com.cat.newname.model_items.Patient_item;
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

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

public class JakaviAllPatients extends AppCompatActivity implements RecyclerTouchListener.RecyclerTouchListenerHelper {


    String url;
    int userId;
    Retrofit retrofit;
    ServiceInterface myInterface;
    private ProgressDialog dialog;

    ArrayList<Patient_item> patients = new ArrayList<>();

    ImageView back;
    RecyclerView recyclerView;

    SharedPreferences shared;

    Patient_adapter adapter;

    ArrayList<String> doctorsNames = new ArrayList<>();
    ArrayList<Integer> doctorsIds = new ArrayList<>();
    SpinnerDialog spinnerDialog;
    TextView chooseDr;
    int currentPageNum = 1;
    int lastPageNum;

    boolean mHasReachedBottomOnce = false;

    int selectedDoctorId;

    // 1 for getPatients     2 for getPatientsByDr
    // 1 for getPatients     2 for getPatientsByDr
    int connecttionType = 1;

    ProgressBar loading;


    private RecyclerTouchListener onTouchListener;
    private OnActivityTouchListener touchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jakavi_all_patients);
        loading = findViewById(R.id.loading);
        chooseDr = findViewById(R.id.chooseDr);
        chooseDr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });
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
        loadPatientsDataByPage(currentPageNum);
        loaadDoctorsData();
        initRecyclerView();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void initDoctorsSpinner() {
        spinnerDialog = new SpinnerDialog(JakaviAllPatients.this, doctorsNames, "Select Doctor", "");// With No Animation
//        spinnerDialog=new SpinnerDialog(JakaviAllPatients.this,items,"Select or Search City",R.style.DialogAnimations_SmileWindow,"Close Button Text");// With 	Animation

        spinnerDialog.setCancellable(true); // for cancellable
        spinnerDialog.setShowKeyboard(false);// for open keyboard by default

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                if (position == 0) {
                    chooseDr.setText(item);
                    currentPageNum = 1;
                    connecttionType = 1;
                    loadPatientsDataByPage(currentPageNum);
                } else {
                    currentPageNum = 1;
                    connecttionType = 2;
                    selectedDoctorId = doctorsIds.get(position);
                    loadPatientsDataOfDoctorByPage(selectedDoctorId, item, currentPageNum);
                }
            }
        });
    }


    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.patientsRecycler);
        adapter = new Patient_adapter(this, patients);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        onTouchListener = new RecyclerTouchListener(this, recyclerView);
        onTouchListener.setClickable(new RecyclerTouchListener.OnRowClickListener() {
            @Override
            public void onRowClicked(int position) {

            }

            @Override
            public void onIndependentViewClicked(int independentViewID, int position) {

            }
        })
                .setLongClickable(true, new RecyclerTouchListener.OnRowLongClickListener() {
                    @Override
                    public void onRowLongClicked(int position) {

                    }
                })
                .setSwipeOptionViews(R.id.edit, R.id.delete)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        if (viewID == R.id.edit) {
                            showDialog(patients.get(position).getProductId(), String.valueOf(patients.get(position).getId()), position);

                        } else if (viewID == R.id.delete) {
                            deletePatient(patients.get(position).getId());
                            patients.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && !mHasReachedBottomOnce) {
                    mHasReachedBottomOnce = true;
                    if (connecttionType == 1) {
                        if (currentPageNum <= lastPageNum)
                            loadMorePatientsDataByPage(currentPageNum);
                    } else if (connecttionType == 2) {
                        if (currentPageNum <= lastPageNum)
                            loadMorePatientsDataOfDoctorByPage(selectedDoctorId, currentPageNum);
                    }
                }
            }
        });
    }

    public void showDialog(String productId, String patientId, int postition) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFragment = new JakaviEditDoseDialog(productId, patientId, postition);
        dialogFragment.show(ft, "dialog");

    }


    public void deletePatient(int patientId) {

        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));

        myInterface.deletePatient(patientId, map).enqueue(new Callback<ResponseBody>() {
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


    public void loaadDoctorsData() {
        myInterface.getDoctors(userId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONArray doctorsArray = responseObject.getJSONArray("data");

                    setDoctorsList(doctorsArray);


                    Log.d("Response", doctorsArray.toString());


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


    public void setDoctorsList(JSONArray list) {
        doctorsIds.add(0);
        doctorsNames.add("All Doctors");
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = currentobject.getInt("id");
                final String name = currentobject.getString("name");
                doctorsIds.add(id);
                doctorsNames.add(name);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initDoctorsSpinner();

    }


    public void loadPatientsDataOfDoctorByPage(int drId, final String drName, int pageNum) {
        dialog.show();
        myInterface.getPatientOfDoctorByPage(drId, pageNum).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    int state = responseObject.getInt("state");
                    if (state == 0) {
                        Toast.makeText(JakaviAllPatients.this, responseObject.getString("data"), Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject patientsObject = responseObject.getJSONObject("data");
                        JSONArray patientsArray = patientsObject.getJSONArray("data");
                        chooseDr.setText(drName);
                        updatePatientsList(patientsArray);
                        Log.d("Response", patientsObject.toString());
                    }
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


    public void loadMorePatientsDataOfDoctorByPage(int drId, int pageNum) {
        loading.setVisibility(View.VISIBLE);
        myInterface.getPatientOfDoctorByPage(drId, pageNum).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    int state = responseObject.getInt("state");
                    if (state == 0) {
                        Toast.makeText(JakaviAllPatients.this, responseObject.getString("data"), Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject patientsObject = responseObject.getJSONObject("data");
                        JSONArray patientsArray = patientsObject.getJSONArray("data");
                        setPatientsList(patientsArray);
                        Log.d("Response", patientsObject.toString());
                    }
                    loading.setVisibility(View.GONE);

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
                loading.setVisibility(View.GONE);
            }
        });
    }


    public void loadPatientsDataByPage(int pageNum) {
        dialog.show();
        myInterface.getPatientsByPage(userId, pageNum).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONObject patientsObject = responseObject.getJSONObject("data");
                    JSONArray patientsArray = patientsObject.getJSONArray("data");
                    lastPageNum = patientsObject.getInt("last_page");
                    setPatientsList(patientsArray);
                    Log.d("Response", patientsArray.toString());
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


    public void loadMorePatientsDataByPage(int pageNum) {
        loading.setVisibility(View.VISIBLE);
        myInterface.getPatientsByPage(userId, pageNum).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONObject patientsObject = responseObject.getJSONObject("data");
                    JSONArray patientsArray = patientsObject.getJSONArray("data");
                    lastPageNum = patientsObject.getInt("last_page");
                    setPatientsList(patientsArray);
                    Log.d("Response", patientsArray.toString());
                    loading.setVisibility(View.GONE);

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
                loading.setVisibility(View.GONE);
            }
        });
    }


    public void setPatientsList(JSONArray list) {
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = currentobject.getInt("id");
                final String name = currentobject.getString("doctor_name");
                final String dose = currentobject.getString("dose_name");
                final String category = currentobject.getString("category_name");
                final String hospital = currentobject.getString("hospital_name");
                final String date = currentobject.getString("created_at");
                final String productId = currentobject.getString("product_id");
                patients.add(new Patient_item(id, name, dose, category, hospital, date, productId));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        mHasReachedBottomOnce = false;
        currentPageNum++;
    }


    public void updatePatient(int position, String dose) {
        patients.get(position).setDose(dose);
        adapter.notifyDataSetChanged();

    }


    public void updatePatientsList(JSONArray list) {
        patients.clear();
        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final int id = currentobject.getInt("id");
                final String name = currentobject.getString("doctor_name");
                final String dose = currentobject.getString("dose_name");
                final String category = currentobject.getString("category_name");
                final String hospital = currentobject.getString("hospital_name");
                final String date = currentobject.getString("created_at");
                final String productId = currentobject.getString("product_id");
                patients.add(new Patient_item(id, name, dose, category, hospital, date, productId));
                adapter.notifyDataSetChanged();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.addOnItemTouchListener(onTouchListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        recyclerView.removeOnItemTouchListener(onTouchListener);
    }
}
