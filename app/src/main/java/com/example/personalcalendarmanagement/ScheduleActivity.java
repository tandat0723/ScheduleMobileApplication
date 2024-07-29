package com.example.personalcalendarmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class ScheduleActivity extends AppCompatActivity {
    private EditText medtTitle, medtDescription, medtDate, medtTime, medtType;
    private Button mbtnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        init();
    }

    private void init() {
        mbtnAdd = findViewById(R.id.btnAdd);
        medtTitle = findViewById(R.id.edtTitle);
        medtDescription = findViewById(R.id.edtDescription);
        medtDate = findViewById(R.id.edtDate);
        medtTime = findViewById(R.id.edtTime);
        medtType = findViewById(R.id.edtType);

        mbtnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(ScheduleActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}