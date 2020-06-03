package com.cat.newname.login;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cat.newname.R;
import com.cat.newname.network_interface.ServiceInterface;
import com.cat.newname.jakavi.JakaviHome;
import com.cat.newname.revolade.RevoladeHome;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText email, password;
    ImageView login;
    CheckBox checkRemeber;
    SharedPreferences shared;

    int idcheck;
    String url;
    Retrofit retrofit;
    ServiceInterface myInterface;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        checkRemeber = findViewById(R.id.chckRemember);

        url = getResources().getString(R.string.services_url);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please, Wait");
        dialog.setCancelable(false);

        retrofit = new Retrofit.Builder()
                .baseUrl(url).
                        addConverterFactory(GsonConverterFactory.create()).build();
        myInterface = retrofit.create(ServiceInterface.class);

        shared = getSharedPreferences("id", Context.MODE_PRIVATE);

        if (shared.contains("id")) {
            idcheck = shared.getInt("id", 0);
        } else {
            idcheck = 0;
        }

        if (idcheck != 0) {
            email.setText(shared.getString("loginName", "0"));
            password.setText(shared.getString("password", "0"));
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }


    public void login() {
        Map<String, String> map = new HashMap<>();
        final String emailtxt = email.getText().toString();
        final String passwordtxt = password.getText().toString();

        if (emailtxt.length() == 0 || passwordtxt.length() == 0) {
            Toast.makeText(Login.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            map.put("email", emailtxt);
            map.put("password", passwordtxt);
            dialog.show();
            myInterface.login(map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    String state = "0";
                    try {
                        JSONObject res = new JSONObject(response.body().string());
                        state = res.getString("state");

                        if (state.equals("0")) {
                            String msg = res.getString("data");
                            Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                        } else if (state.equals("1")) {
                            JSONObject userData = res.getJSONObject("data");
                            SharedPreferences.Editor myEdit = shared.edit();
                            myEdit.putInt("id", userData.getInt("id"));
                            myEdit.putString("name", userData.getString("username"));
                            myEdit.putString("email", userData.getString("email"));
                            myEdit.putString("type", userData.getString("type"));
                            myEdit.putInt("scored", userData.getInt("score"));
                            myEdit.putInt("target", userData.getInt("target"));
                            myEdit.putString("percentage", userData.getString("percentage"));
                            myEdit.putString("loginName", emailtxt);
                            if (checkRemeber.isChecked()) {
                                myEdit.putString("password", passwordtxt);
                            } else {
                                myEdit.putString("password", "");
                            }
                            myEdit.commit();
                            if (userData.getString("type").equals("jakavi")) {
                                Intent i = new Intent(getApplicationContext(), JakaviHome.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(getApplicationContext(), RevoladeHome.class);
                                startActivity(i);
                            }
                            finish();
                        }
                        dialog.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(Login.this, "Network Error", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }
}
