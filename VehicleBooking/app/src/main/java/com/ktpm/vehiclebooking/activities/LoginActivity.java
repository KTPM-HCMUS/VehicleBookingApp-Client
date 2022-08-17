package com.ktpm.vehiclebooking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.ktpm.vehiclebooking.Constants;
import com.ktpm.vehiclebooking.R;
import com.ktpm.vehiclebooking.api.LoginAPI;
import com.ktpm.vehiclebooking.database.DBHandler;
import com.ktpm.vehiclebooking.model.LoginModel;
import com.ktpm.vehiclebooking.model.ResponseTT;
import com.ktpm.vehiclebooking.model.Result;
import com.ktpm.vehiclebooking.model.User;
import com.ktpm.vehiclebooking.model.UserTemp;
import com.ktpm.vehiclebooking.utilities.JWTUtils;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    EditText phoneEditText, passwordEditText;
    TextView moveToRegister;
    private DBHandler handler = new DBHandler(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        linkViewElements();
        setRegisterTextViewAction();
        setLoginBtnAction();
    }

    //Get View variables from xml id
    private void linkViewElements() {
        loginBtn = findViewById(R.id.loginLoginBtn);
        phoneEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        moveToRegister = findViewById(R.id.moveToRegisterTextView);
    }

    //Login process when clicking 'login' button
    private void setLoginBtnAction() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = phoneEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                if (phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, Constants.ToastMessage.emptyInputError,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                LoginWithUserIdAndPassword(phone, password);
            }
        });
    }

    private void LoginWithUserIdAndPassword(String phoneNumber, String password){
        LoginModel user = new LoginModel(phoneNumber, password);
        LoginAPI.apiService.login(user).enqueue(new Callback<ResponseTT>() {
            @Override
            public void onResponse(Call<ResponseTT> call, Response<ResponseTT> response) {
                success(response);
            }

            @Override
            public void onFailure(Call<ResponseTT> call, Throwable t) {
                failure();
            }
        });
    }
    private void loginWithToken(){
        ArrayList<String> s = handler.readDB();
        if(s.size() != 0){
            checkLogin(handler.readDB().get(1));
        }
    }

    private void checkLogin(String token){
        LoginAPI.apiService.revokeToken("Bearer " + token).enqueue(new Callback<ResponseTT>() {
            @Override
            public void onResponse(Call<ResponseTT> call, Response<ResponseTT> response) {
                success(response);
            }

            @Override
            public void onFailure(Call<ResponseTT> call, Throwable t) {
                failure();
            }
        });
    }


    private void getRefreshToken(String userId){
        ArrayList<String> list = handler.readDB();
        String refreshToken = list.get(0);
        UserTemp userTemp = new UserTemp(userId, refreshToken);
        LoginAPI.apiService.getNewToken(userTemp).enqueue(new Callback<ResponseTT>() {
            @Override
            public void onResponse(Call<ResponseTT> call, Response<ResponseTT> response) {
                success(response);
            }

            @Override
            public void onFailure(Call<ResponseTT> call, Throwable t) {
                failure();
            }
        });

    }



    private void success(Response<ResponseTT> response){
        ArrayList<String> s = handler.readDB();
        User user = JWTUtils.parseTokenToGetUser(response.body().getResult().getToken());
        try {
            String isValid = response.body().getResult().getLoginError();
            String refreshToken = response.body().getResult().getRefreshToken();
            String token = response.body().getResult().getToken();
            if(isValid.equals("SUCCESS")){
                writeDB(refreshToken, token, isValid);
                Toast.makeText(LoginActivity.this, Constants.ToastMessage.signInSuccess,
                        Toast.LENGTH_SHORT).show();
                moveToHomePage();
            }else{
                if(s.size() != 0){
                    getRefreshToken(user.getUserID());
                }
                Toast.makeText(LoginActivity.this, Constants.ToastMessage.signInFailure,
                        Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            getRefreshToken(user.getUserID());
        }

    }

    private void failure(){
        Toast.makeText(LoginActivity.this, Constants.ToastMessage.signInFailure,
                Toast.LENGTH_SHORT).show();
    }

    public void writeDB(String refreshToken, String token, String isValid){
        Result result = new Result(refreshToken, token, isValid);
        ArrayList<String> s  = handler.readDB();
        if(s.size()==0){
            handler.addNewToken(refreshToken, token);
        }else{
            handler.update(refreshToken, token);
        }
    }

    //Move to user's homepage if successfully logged in
    private void moveToHomePage() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.putExtra("email", phoneEditText.getText().toString());
        startActivity(i);
        finish();
    }

    private void setRegisterTextViewAction() {
        moveToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                loginWithToken();
                finish();
            }
        });
    }
}