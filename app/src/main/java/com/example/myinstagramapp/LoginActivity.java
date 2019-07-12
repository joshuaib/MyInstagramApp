package com.example.myinstagramapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser() != null) {
            final Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);
        usernameInput = findViewById(R.id.signUpUsername);
        passwordInput = findViewById(R.id.signUpPassword);
        loginBtn = findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener((v) -> {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();
                login(username, password);
        });
    }
    public void login(String username, String password) {
        ParseUser.logInInBackground(username, password, (ParseUser user, ParseException e) -> {
            if (e == null) {
                Log.d("LoginActivity", "Login Success!");
                final Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Log.e("LoginActivity", "Login Failure");
                e.printStackTrace();
            }
        });
    }
    public void signUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
