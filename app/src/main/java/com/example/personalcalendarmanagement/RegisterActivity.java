package com.example.personalcalendarmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText medtFullName, medtUserName, medtPassword, medtConfirmPassword;
    private Button mbtnBack, mbtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        medtFullName = findViewById(R.id.edtFullName);
        medtUserName = findViewById(R.id.edtUserName);
        medtPassword = findViewById(R.id.edtPassword);
        medtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        mbtnRegister = findViewById(R.id.btnRegister);
        mbtnBack = findViewById(R.id.btnBack);

        mbtnBack.setOnClickListener(view -> {
            finish();
        });

        mbtnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            Toast.makeText(this, R.string.noti_register_success, Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
    }
}