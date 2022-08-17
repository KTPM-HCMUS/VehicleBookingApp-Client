package com.ktpm.vehiclebooking.activities;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ktpm.vehiclebooking.Constants;
import com.ktpm.vehiclebooking.R;
import com.ktpm.vehiclebooking.api.LoginAPI;
import com.ktpm.vehiclebooking.database.DBHandler;
import com.ktpm.vehiclebooking.model.ResponseTT;
import com.ktpm.vehiclebooking.model.User;
import com.ktpm.vehiclebooking.utilities.JWTUtils;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFinalActivity extends AppCompatActivity {

    Button backBtn, registerBtn;
    EditText phoneEditText, passwordEditText;
    private String email, username, birthDate, vehiclePlateNumber;
    private int role, transportationType;
    private DBHandler dbHandler = new DBHandler(RegisterFinalActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_final);
        linkViewElements();
        getPreviousRegisterFormInfo();
        setBackBtnAction();
        setRegisterBtnAction();
    }

    private void linkViewElements() {
        backBtn = (Button) findViewById(R.id.registerFinalBackBtn);
        registerBtn = (Button) findViewById(R.id.registerFinalRegisterBtn);
        phoneEditText = (EditText) findViewById(R.id.registerFinalPhoneEditText);
        passwordEditText = (EditText) findViewById(R.id.registerFinalPasswordEditText);
    }

    private void setBackBtnAction() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterFinalActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void getPreviousRegisterFormInfo() {
        Intent intent = getIntent();
        username = (String) intent.getExtras().get(Constants.FSUser.usernameField);
        email = (String) intent.getExtras().get(Constants.FSUser.emailField);
        birthDate = (String) intent.getExtras().get(Constants.FSUser.birthDateField);
        role = (int) intent.getExtras().get(Constants.FSUser.roleField);
        transportationType = (int) intent.getExtras().get(Constants.FSUser.transportationType);
        vehiclePlateNumber = (String) intent.getExtras().get(Constants.FSUser.vehiclePlateNumber);
    }

    private void moveToLoginActivity() {
        Intent i = new Intent(RegisterFinalActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void setRegisterBtnAction() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterFinalActivity.this, Constants.ToastMessage.emptyInputError, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (vehiclePlateNumber.isEmpty()){
                    vehiclePlateNumber = "";
                }
                User user = new User(phone, password, username, birthDate, email, role, transportationType, vehiclePlateNumber);
                LoginAPI.apiService.register(user).enqueue(new Callback<ResponseTT>() {
                    @Override
                    public void onResponse(Call<ResponseTT> call, Response<ResponseTT> response) {
                        if(response.isSuccessful()){
                            try {
                                String isValid = response.body().getResult().getLoginError();
                                System.out.println(isValid);
                                String refreshToken = response.body().getResult().getRefreshToken();
                                System.out.println();
                                String token = response.body().getResult().getToken();
                                System.out.println(token);
                                System.out.println();
                                User user = JWTUtils.parseTokenToGetUser(token);
                                if(isValid.equals("SUCCESS")){
                                    writeDB(refreshToken, token, isValid);
                                    Toast.makeText(RegisterFinalActivity.this, Constants.ToastMessage.registerSuccess, Toast.LENGTH_SHORT).show();
                                    moveToLoginActivity();
                                }
                                else{
                                    Toast.makeText(RegisterFinalActivity.this, Constants.ToastMessage.registerFailure,
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                            catch (Exception e){
                                Toast.makeText(RegisterFinalActivity.this, Constants.ToastMessage.registerFailure,
                                        Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseTT> call, Throwable t) {
                        Toast.makeText(RegisterFinalActivity.this, Constants.ToastMessage.registerFailure,
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    public void writeDB(String refreshToken, String token, String isValid){
        ArrayList<String> s  = dbHandler.readDB();
        if(s.size()==0){
            dbHandler.addNewToken(refreshToken, token);
        }else{
            dbHandler.update(refreshToken, token);
        }
    }

}