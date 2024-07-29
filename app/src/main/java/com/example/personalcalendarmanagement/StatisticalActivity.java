package com.example.personalcalendarmanagement;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.Calendar;

public class StatisticalActivity extends AppCompatActivity {
    private EditText medtStartDate, medtEndDate, medtStartTime, medtEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical);
        init();
    }

    private void init() {
        medtStartDate = findViewById(R.id.edtStartDate);
        medtEndDate = findViewById(R.id.edtEndDate);
        medtStartTime = findViewById(R.id.edtStartTime);
        medtEndTime = findViewById(R.id.edtEndTime);

        medtStartDate.setOnClickListener(view -> {
            showDatePickerDialog();
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, selectedY, selectedM, selectedD) -> {
            String selectedDate = selectedD + "/" + (selectedM + 1) + "/" + selectedY;
            medtStartDate.setText(selectedDate);
        }, y, m, d);

        dialog.show();
    }
}