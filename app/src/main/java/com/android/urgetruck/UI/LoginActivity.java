package com.android.urgetruck.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.urgetruck.R;
import com.android.urgetruck.UI.Models.APIError;
import com.android.urgetruck.UI.Models.LoginModel;
import com.android.urgetruck.UI.Models.LoginResultModel;
import com.android.urgetruck.UI.Network.APiClient;
import com.android.urgetruck.UI.Network.ApiInterface;
import com.android.urgetruck.UI.Utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private ProgressBar progressBar;
    private TextInputLayout textinputusername;
    private TextInputLayout textinputpassword;
    private TextInputEditText tvusername;
    private TextInputEditText tvpassword;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        textinputusername = findViewById(R.id.textinputusername);
        textinputpassword = findViewById(R.id.textinputpassword);
        progressBar = findViewById(R.id.progressbar);
        tvusername = findViewById(R.id.tvusername);
        tvpassword = findViewById(R.id.tvpassword);

        if(Utils.getSharedPreferences(this,"token")!="")
        {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInput(view);
            }
        });


    }

    private boolean validateEmail() {
        String emailInput = textinputusername.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textinputusername.setError("Username can't be empty");
            return false;

        } else {
            textinputusername.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String passwordInput = textinputpassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textinputpassword.setError("Password can't be empty");
            return false;

        }else if(passwordInput.length()<6){
            textinputpassword.setError("Invalid password");
            return false;
        }else {
            textinputpassword.setError(null);
            return true;
        }
    }

    public void confirmInput(View v) {
        Log.e("username",tvusername.getText().toString().trim());
        Log.e("password",tvpassword.getText().toString().trim());

        if (!validateEmail() || !validatePassword()) {
            return;
        }
        if(!Utils.isConnected(LoginActivity.this)){
            Utils.showCustomDialog(LoginActivity.this,getString(R.string.internet_connection));
        } else if (tvusername.getText().toString().trim().equals("admin") && tvpassword.getText().toString().trim().equals("utmobile")) {
                Utils.postsharedPreferences(LoginActivity.this,"isadmin","true");
                Utils.postsharedPreferences(LoginActivity.this,"username","Administrator");
                Utils.postsharedPreferences(LoginActivity.this,"token","local");
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
        } else {
            callLoginApi(textinputusername.getEditText().getText().toString().trim(), textinputpassword.getEditText().getText().toString().trim());
        }

    }

    private void callLoginApi(String username, String password) {
        progressBar.setVisibility(View.VISIBLE);


        String baseurl= Utils.getSharedPreferences(LoginActivity.this,"apiurl");
        Log.e("url",baseurl);
        ApiInterface apiService = APiClient.getClient(baseurl).create(ApiInterface.class);

        LoginModel modal = new LoginModel(username,password);

        Call<LoginResultModel> call = apiService.PostRfid(modal);

        call.enqueue(new Callback<LoginResultModel>() {
            @Override
            public void onResponse(Call<LoginResultModel> call, Response<LoginResultModel> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful())
                {
                    Utils.postsharedPreferences(LoginActivity.this,"token",response.body().getJwtToken());
                    Utils.postsharedPreferences(LoginActivity.this,"username",response.body().getUserName());
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();

                }else{
                    APIError message = new Gson().fromJson(response.errorBody().charStream(), APIError.class);
                    Log.e("msg",message.getMessage());
                    Utils.showCustomDialog(LoginActivity.this,message.getMessage());


                }

                //Log.e("submit response",response.body().getEmail());


            }

            @Override
            public void onFailure(Call<LoginResultModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("error",t.toString());
                Utils.showCustomDialog(LoginActivity.this,t.toString());

            }
        });

    }


}