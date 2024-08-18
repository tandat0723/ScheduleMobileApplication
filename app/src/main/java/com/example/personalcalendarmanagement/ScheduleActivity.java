package com.example.personalcalendarmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

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
            String title = medtTitle.getText().toString();
            String description = medtDescription.getText().toString();
            String date = medtDate.getText().toString();
            String time = medtTime.getText().toString();

            Intent intent = new Intent(ScheduleActivity.this, HomeActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("description", description);
            intent.putExtra("date", date);
            intent.putExtra("time", time);
            startActivity(intent);
        });

        medtDate.setOnClickListener(view -> {
            showDatePickerDialog();
        });

        medtTime.setOnClickListener(view -> {
            showTimePickerDialog();
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, selectedY, selectedM, selectedD) -> {
            String selectedDate = selectedD + "/" + (selectedM + 1) + "/" + selectedY;
            medtDate.setText(selectedDate);
        }, y, m, d);

        dialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
            String time = String.format("%02d:%02d", selectedHour, selectedMinute);
            medtTime.setText(time);
        }, hour, minute, true);

        timePickerDialog.show();
    }
}