package com.example.personalcalendarmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Button mbtnLogin;
    private TextView mtxtRegister, mtxtForgetPassword;
    private EditText medtName, medtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        mtxtRegister = findViewById(R.id.txtRegister);
        mbtnLogin = findViewById(R.id.btnLogin);

        mtxtRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        mbtnLogin.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
        });
    }
}