package com.example.personalcalendarmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.personalcalendarmanagement.data.DatabaseHelper;

public class ForgetPasswordActivity extends AppCompatActivity {
    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        init();
    }
    private void init() {
        helper = new DatabaseHelper(this);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}