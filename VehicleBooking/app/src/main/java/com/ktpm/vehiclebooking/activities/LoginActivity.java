package com.ktpm.vehiclebooking.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ktpm.vehiclebooking.Constants;
import com.ktpm.vehiclebooking.R;

public class LoginActivity extends AppCompatActivity {
    Button backBtn, loginBtn;
    EditText emailEditText, passwordEditText;
    TextView moveToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        linkViewElements();
//        setBackBtnAction();
        setRegisterTextViewAction();
        setLoginBtnAction();
    }

    //Get View variables from xml id
    private void linkViewElements() {
//        backBtn = findViewById(R.id.loginBackBtn);
        loginBtn = findViewById(R.id.loginLoginBtn);
        emailEditText = findViewById(R.id.loginEmailEditText);
        passwordEditText = findViewById(R.id.loginPasswordEditText);
        moveToRegister = findViewById(R.id.moveToRegisterTextView);
    }

    //Login process when clicking 'login' button
    private void setLoginBtnAction() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                //Check if the input email or password is empty
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, Constants.ToastMessage.emptyInputError,
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Auth instance
            }
        });
    }

    //Move to user's homepage if successfully logged in
    private void moveToHomePage() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.putExtra("email", emailEditText.getText().toString());
        startActivity(i);
        finish();
    }

    private void setRegisterTextViewAction() {
        moveToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}