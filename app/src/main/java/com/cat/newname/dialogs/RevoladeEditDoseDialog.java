package com.cat.newname.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import com.cat.newname.R;
import com.cat.newname.network_interface.ServiceInterface;
import com.cat.newname.revolade.RevoladeAllPatients;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RevoladeEditDoseDialog extends DialogFragment {


    ArrayList<String> dosesText = new ArrayList<>();
    ArrayList<String> dosesId = new ArrayList<>();
    int userId;
    SharedPreferences shared;

    String url;
    Retrofit retrofit;
    ServiceInterface myInterface;

    String productId, patientId;
    int itemClickedposition;
    Button done, cancel;
    ProgressBar loading;
    AppCompatSpinner spinner;

    public RevoladeEditDoseDialog(String productId, String patientId, int itemClickedposition) {
        this.productId = productId;
        this.patientId = patientId;
        this.itemClickedposition = itemClickedposition;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_dose_dialog, container, false);


        shared = getActivity().getSharedPreferences("id", Context.MODE_PRIVATE);
        userId = shared.getInt("id", 0);

        url = getResources().getString(R.string.services_url);

        retrofit = new Retrofit.Builder()
                .baseUrl(url).
                        addConverterFactory(GsonConverterFactory.create()).build();
        myInterface = retrofit.create(ServiceInterface.class);

        loadDoses();

        spinner = view.findViewById(R.id.dosesSpinner);

        done = view.findViewById(R.id.btnDone);
        cancel = view.findViewById(R.id.btnCancel);
        loading = view.findViewById(R.id.loading);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDose();
            }
        });

        return view;
    }


    public void updateDose() {

        loading.setVisibility(View.VISIBLE);


        Map<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));
        map.put("dose_id", dosesId.get(spinner.getSelectedItemPosition()));

        Log.d("map", map.toString() + "patientId " + patientId);
        myInterface.updatePatientDose(patientId, map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {

                    RevoladeAllPatients parentActivity = (RevoladeAllPatients) getActivity();
                    JSONObject responseObject = new JSONObject(response.body().string());
                    String msg = responseObject.getString("data");

                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    parentActivity.updatePatient(itemClickedposition, dosesText.get(spinner.getSelectedItemPosition()));

                    dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
            }
        });

    }


    public void loadDoses() {


//        loading.setVisibility(View.VISIBLE);
        myInterface.getDoses(productId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    JSONObject responseObject = new JSONObject(response.body().string());
                    JSONArray dosesArray = responseObject.getJSONArray("data");

                    if (dosesArray.length() == 0) {
                        Toast.makeText(getActivity(), "No Doses For this Patient", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        setDosesList(dosesArray);


                        Log.d("Response", dosesArray.toString());

                        loading.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error Throw", t.toString());
                Log.d("commit Test Throw", t.toString());
                Log.d("Call", t.toString());
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                dismiss();
            }
        });
    }


    public void setDosesList(JSONArray list) {


        try {

            for (int i = 0; i < list.length(); i++) {
                JSONObject currentobject = list.getJSONObject(i);
                final String id = currentobject.getString("id");
                final String name = currentobject.getString("name");
                dosesText.add(name);
                dosesId.add(id);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initDosesSpinner();

    }


    private void initDosesSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, dosesText);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}
