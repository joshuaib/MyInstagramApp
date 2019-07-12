package com.example.myinstagramapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class SignUpActivity extends AppCompatActivity {
    private EditText signUpUsername;
    private EditText signUpPassword;
    private EditText emailInput;
    private EditText phoneInput;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpUsername = findViewById(R.id.signUpUsername);
        signUpPassword = findViewById(R.id.signUpPassword);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        btnSignUp = findViewById(R.id.btnLogin);

        btnSignUp.setOnClickListener((view) -> {
            final String username = signUpUsername.getText().toString();
            final String password = signUpPassword.getText().toString();
            final String email = emailInput.getText().toString();
            final String phoneNumber = phoneInput.getText().toString();
            //feed them into the sign up method
            signUpUser(username, password, email, phoneNumber);
        });
    }
    public void signUpUser(String username, String password, String email, String phoneNo) {
        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        user.put("phone", phoneNo);
        user.signUpInBackground((e) -> {
            if (e == null) {
                Log.d("LoginActivity", "Sign Up Success!");
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.e("LoginActivity", "Sign Up failure");
                e.printStackTrace();
            }
        });
    }
}