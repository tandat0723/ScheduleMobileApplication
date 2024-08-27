package com.example.personalcalendarmanagement;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalcalendarmanagement.fragment.HomeFragment;

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
            String title = medtTitle.getText().toString().trim();
            String description = medtDescription.getText().toString().trim();
            String date = medtDate.getText().toString().trim();
            String time = medtTime.getText().toString().trim();
            String type = medtType.getText().toString().trim();

            if (title.isEmpty() || description.isEmpty() || type.isEmpty() || date.isEmpty() || time.isEmpty()) {
                if (title.isEmpty()) {
                    medtTitle.requestFocus();
                } else if (description.isEmpty()) {
                    medtDescription.requestFocus();
                } else if (type.isEmpty()) {
                    medtType.requestFocus();
                } else if (date.isEmpty()) {
                    medtDate.requestFocus();
                } else {
                    medtTime.requestFocus();
                }

                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(ScheduleActivity.this, HomeFragment.class);
            startActivity(intent);
            saveDataBase(title, description, type, date, time);
        });

        medtDate.setOnClickListener(view -> {
            showDatePickerDialog();
        });

        medtTime.setOnClickListener(view -> {
            showTimePickerDialog();
        });
    }

    private void saveDataBase(String title, String description, String type, String date, String time) {
        Toast.makeText(this, "Thêm lịch trình thành công", Toast.LENGTH_SHORT).show();
        finish();
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