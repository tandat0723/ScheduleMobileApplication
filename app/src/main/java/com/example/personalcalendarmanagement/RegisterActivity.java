package com.example.personalcalendarmanagement;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalcalendarmanagement.Utils.Utils;
import com.example.personalcalendarmanagement.data.DBHelper;
import com.example.personalcalendarmanagement.data.MyDatabase;
import com.example.personalcalendarmanagement.data.Roles;
import com.example.personalcalendarmanagement.data.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText medtFullName, medtUserName, medtPassword, medtConfirmPassword;
    private Button mbtnBack, mbtnRegister;
    private DBHelper helper;
    private MyDatabase myDatabase;

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

        myDatabase = new MyDatabase(this);

        mbtnBack.setOnClickListener(view -> {
            finish();
        });

        mbtnRegister.setOnClickListener(view -> {
            String username = medtUserName.getText().toString();
            String password = medtPassword.getText().toString();
            String confirmPassword = medtConfirmPassword.getText().toString();
            String fullName = medtFullName.getText().toString();

            String checkError = validate(username, password, confirmPassword, fullName);
            if (checkError != null) {
                Toast.makeText(this, checkError, Toast.LENGTH_SHORT).show();
                return;
            }

            if (myDatabase.userExists(username)) {
                Toast.makeText(this, R.string.error_user_exists, Toast.LENGTH_SHORT).show();
                return;
            }

            Roles roleId = Roles.USER;
            User user = new User(fullName, username, Utils.hashPassword(password), roleId);
            long result = myDatabase.addUser(user);

            if (result > 0) {
                Toast.makeText(this, R.string.noti_register_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.noti_register_fail, Toast.LENGTH_SHORT).show();
            }

        });
    }

    private String validate(String username, String password, String confirmPassword, String fullName) {
        if (!password.equals(confirmPassword)) {
            return getString(R.string.error_password_confirm_mismatch);
        }
        if (username.isEmpty() || fullName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            if (fullName.isEmpty()) {
                medtFullName.requestFocus();
                return getString(R.string.error_fullame_empty);
            } else if (username.isEmpty()) {
                medtUserName.requestFocus();
                return getString(R.string.error_username_empty);
            } else if (password.isEmpty()) {
                medtPassword.requestFocus();
                return getString(R.string.error_password_empty);
            } else {
                medtConfirmPassword.requestFocus();
                return getString(R.string.error_confirm_password_empty);
            }
        }
        if (!password.matches(".*[A-Z].*")) {
            return getString(R.string.error_password_uppercase);
        }

        if (!password.matches(".*[a-z].*")) {
            return getString(R.string.error_password_lowercase);
        }

        if (!password.matches(".*\\d.*")) {
            return getString(R.string.error_password_digit);
        }

//        if (!password.matches(".*[!@#$%^&*+=?-].*")) {
//            return getString(R.string.error_password_special_char);
//        }
        return null;
    }
}