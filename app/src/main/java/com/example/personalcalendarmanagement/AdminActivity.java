package com.example.personalcalendarmanagement;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalcalendarmanagement.Utils.Utils;
import com.example.personalcalendarmanagement.adapter.CustomAdapterAdmin;
import com.example.personalcalendarmanagement.data.MyDatabase;
import com.example.personalcalendarmanagement.data.Roles;
import com.example.personalcalendarmanagement.data.User;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private ListView lvUser;
    private MyDatabase myDatabase;
    private List<User> list;
    private CustomAdapterAdmin adapter;
    private Button btnLogout, btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnLogout = findViewById(R.id.btnLogoutAdmin);
        btnAddUser = findViewById(R.id.btnAddUser);
        lvUser = findViewById(R.id.lvUser);
        myDatabase = new MyDatabase(this);
        loadDataUser();
        init();
    }

    private void init() {
        btnLogout.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnAddUser.setOnClickListener(view -> {
            addUserByDialog();
        });
    }

    private void addUserByDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_user, null);
        builder.setView(dialogView);

        EditText edtFullName = dialogView.findViewById(R.id.edtFullName);
        EditText edtUsername = dialogView.findViewById(R.id.edtUsername);
        EditText edtPassword = dialogView.findViewById(R.id.edtPassword);
        Spinner spinnerRole = dialogView.findViewById(R.id.spinnerRole);
        Button btnSaveUser = dialogView.findViewById(R.id.btnSaveUser);
        Button btnClose = dialogView.findViewById(R.id.btnClose);

        Roles[] allowedRoles = {Roles.User, Roles.Admin};
        ArrayAdapter<Roles> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allowedRoles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        AlertDialog dialog = builder.create();
        btnSaveUser.setOnClickListener(view -> {
            String fullName = edtFullName.getText().toString().trim();
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            Roles selectedRole = (Roles) spinnerRole.getSelectedItem();

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            String checkError = validate(password);
            if (checkError != null){
                Toast.makeText(this, checkError, Toast.LENGTH_SHORT).show();
                return;
            }

            if (myDatabase.userExists(username)) {
                Toast.makeText(this, R.string.error_user_exists, Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(fullName, username, Utils.hashPassword(password), selectedRole);
            myDatabase.addUser(newUser);
            Toast.makeText(this, "Thêm tài khoản thành công", Toast.LENGTH_SHORT).show();

            loadDataUser();
            dialog.dismiss();

        });

        btnClose.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void loadDataUser() {
        list = myDatabase.getAllUser();
        if (list != null && !list.isEmpty()) {
            adapter = new CustomAdapterAdmin(this, list, myDatabase);
            lvUser.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Không có người dùng nào.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private String validate(String password) {
        if (!password.matches(".*[A-Z].*")) {
            return getString(R.string.error_password_uppercase);
        }

        if (!password.matches(".*[a-z].*")) {
            return getString(R.string.error_password_lowercase);
        }

        if (!password.matches(".*\\d.*")) {
            return getString(R.string.error_password_digit);
        }
        return null;
    }


}