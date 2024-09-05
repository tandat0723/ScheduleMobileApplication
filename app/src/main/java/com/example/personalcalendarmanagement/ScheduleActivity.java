package com.example.personalcalendarmanagement;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalcalendarmanagement.Utils.ReminderBroadcastReceiver;
import com.example.personalcalendarmanagement.data.MyDatabase;
import com.example.personalcalendarmanagement.data.Schedule;
import com.example.personalcalendarmanagement.fragment.OnScheduleAddedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {
    private EditText medtTitle, medtDescription, medtDate, medtTime, medtType;
    private Button mbtnAdd;
    private MyDatabase myDatabase;


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
        myDatabase = new MyDatabase(this);

        mbtnAdd.setOnClickListener(view -> {
            String title = medtTitle.getText().toString().trim();
            String description = medtDescription.getText().toString().trim();
            String date = medtDate.getText().toString().trim();
            String time = medtTime.getText().toString().trim();
            String type = medtType.getText().toString().trim();

            String checkError = validate(title, description, type, date, time);
            if (checkError != null) {
                Toast.makeText(this, checkError, Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int userId = sharedPreferences.getInt("user_id", -1);
            if (userId == -1) {
                Toast.makeText(this, "Tài khoản không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            Schedule schedule = new Schedule(title, description, type, date, time, userId);
            if (this instanceof OnScheduleAddedListener) {
                ((OnScheduleAddedListener) this).onScheduleAdded(schedule);
            }

            long result = myDatabase.addSchedule(schedule);

            if (result > 0) {
                Intent intent = new Intent();
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("type", type);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                setResult(Activity.RESULT_OK, intent);

//                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
//                try {
//                    String dateTimeString = date + " " + time;
//                    Date dateTime = format.parse(dateTimeString);
//                    if (dateTime != null) {
//                        long timeIn = dateTime.getTime();
//                        setReminder(getApplicationContext(), timeIn);
//                    } else {
//                        Toast.makeText(this, "Không thể thiết lập thông báo do lỗi ngày giờ.", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

                Toast.makeText(this, R.string.noti_add_schedule_success, Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(this, R.string.noti_add_schdule_fail, Toast.LENGTH_SHORT).show();
            }

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

    private String validate(String title, String description, String type, String date, String time) {
        if (title.isEmpty() || description.isEmpty() || type.isEmpty() || date.isEmpty() || time.isEmpty()) {
            if (title.isEmpty()) {
                medtTitle.requestFocus();
                return getString(R.string.error_schedule_title);
            } else if (description.isEmpty()) {
                medtDescription.requestFocus();
                return getString(R.string.error_schedule_description);
            } else if (type.isEmpty()) {
                medtType.requestFocus();
                return getString(R.string.error_schedule_type);
            } else if (date.isEmpty()) {
                medtDate.requestFocus();
                return getString(R.string.error_schedule_date);
            } else {
                medtTime.requestFocus();
                return getString(R.string.error_schedule_time);
            }
        }

        return null;
    }

    private void setReminder(Context context, long time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

}