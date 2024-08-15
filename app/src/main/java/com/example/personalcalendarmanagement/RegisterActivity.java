package com.example.personalcalendarmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.personalcalendarmanagement.data.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText medtFullName, medtUserName, medtPassword, medtConfirmPassword;
    private Button mbtnBack, mbtnRegister;
    DatabaseHelper helper;

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

        helper = new DatabaseHelper(this);

        mbtnBack.setOnClickListener(view -> {
            finish();
        });

        mbtnRegister.setOnClickListener(view -> {
            String username = medtUserName.getText().toString();
            String password = medtPassword.getText().toString();
            String confirmPassword = medtConfirmPassword.getText().toString();
            String fullName = medtFullName.getText().toString();
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, R.string.noti_register_confirm, Toast.LENGTH_SHORT).show();
                return;
            }
            int roleId = 2;
            long result = helper.addUser(username, password, fullName, roleId);
            if (result > 0) {
                Toast.makeText(this, R.string.noti_register_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.noti_register_fail, Toast.LENGTH_SHORT).show();
            }

        });
    }
}