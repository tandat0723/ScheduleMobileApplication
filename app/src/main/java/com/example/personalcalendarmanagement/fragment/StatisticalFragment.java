package com.example.personalcalendarmanagement.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personalcalendarmanagement.R;
import com.example.personalcalendarmanagement.adapter.CustomAdapterStatistical;
import com.example.personalcalendarmanagement.data.MyDatabase;
import com.example.personalcalendarmanagement.data.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatisticalFragment extends Fragment {
    private EditText edtStartDate, edtEndDate, edtStartTime, edtEndTime, edtSearch;
    private Button btnSearch;
    private TextView mTxtEmpty;
    private ListView lvStatistical;
    private MyDatabase myDatabase;
    private CustomAdapterStatistical adapter;
    private List<Schedule> list;
    private int userId;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_statistical_fragment, container, false);

        lvStatistical = root.findViewById(R.id.lvStatistical);
        mTxtEmpty = root.findViewById(R.id.txtEmpty);
        edtStartDate = root.findViewById(R.id.edtStartDate);
        edtEndDate = root.findViewById(R.id.edtEndDate);
        edtStartTime = root.findViewById(R.id.edtStartTime);
        edtEndTime = root.findViewById(R.id.edtEndTime);
        edtSearch = root.findViewById(R.id.edtSearch);
        btnSearch = root.findViewById(R.id.btnSearch);

        list = new ArrayList<>();
        myDatabase = new MyDatabase(getActivity());
        adapter = new CustomAdapterStatistical(getActivity(), list, myDatabase);

        loadSchedule();
        init();

        return root;
    }

    private void init() {
        btnSearch.setOnClickListener(view -> {
            String keyword = edtSearch.getText().toString().trim();
            String startDate = edtStartDate.getText().toString().trim();
            String endDate = edtEndDate.getText().toString().trim();
            String startTime = edtStartTime.getText().toString().trim();
            String endTime = edtEndTime.getText().toString().trim();

            if (!keyword.isEmpty()) {
                searchScheduleByTitle(keyword);

            } else if (!startDate.isEmpty() && !endDate.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()) {
                filterScheduleByDateTime(startDate, endDate, startTime, endTime);

            } else {
                Toast.makeText(getContext(), "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();

            }
        });

        edtStartDate.setOnClickListener(view -> {
            showDatePickerDialogStart();
        });

        edtEndDate.setOnClickListener(view -> {
            showDatePickerDialogEnd();
        });

        edtStartTime.setOnClickListener(view -> {
            showTimePickerDialogStart();
        });

        edtEndTime.setOnClickListener(view -> {
            showTimePickerDialogEnd();
        });
    }

    private void showDatePickerDialogStart() {
        final Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(), (view, selectedY, selectedM, selectedD) -> {
            String selectedDate = selectedD + "/" + (selectedM + 1) + "/" + selectedY;
            edtStartDate.setText(selectedDate);
        }, y, m, d);

        dialog.show();
    }

    private void showDatePickerDialogEnd() {
        final Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(), (view, selectedY, selectedM, selectedD) -> {
            String selectedDate = selectedD + "/" + (selectedM + 1) + "/" + selectedY;
            edtEndDate.setText(selectedDate);
        }, y, m, d);

        dialog.show();
    }

    private void showTimePickerDialogStart() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, selectedHour, selectedMinute) -> {
            String time = String.format("%02d:%02d", selectedHour, selectedMinute);
            edtStartTime.setText(time);

        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void showTimePickerDialogEnd() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, selectedHour, selectedMinute) -> {
            String time = String.format("%02d:%02d", selectedHour, selectedMinute);
            edtEndTime.setText(time);

        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void loadSchedule() {
        userId = getCurrentUserId();
        list.clear();
        list.addAll(myDatabase.getAllScheduleByUser(userId));
        adapter.notifyDataSetChanged();
    }

    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    private void searchScheduleByTitle(String keyword) {
        List<Schedule> filterList = myDatabase.getSchedulesByTitle(keyword, userId);
        if (filterList.isEmpty()) {
            mTxtEmpty.setVisibility(View.VISIBLE);
            lvStatistical.setVisibility(View.GONE);
        } else {
            mTxtEmpty.setVisibility(View.GONE);
            lvStatistical.setVisibility(View.VISIBLE);
            adapter.updateList(filterList);
        }
    }

    private void filterScheduleByDateTime(String startDate, String endDate, String startTime, String endTime) {
        List<Schedule> filterList = myDatabase.filterScheduleByDateTime(startDate, endDate, startTime, endTime);
        if (filterList.isEmpty()) {
            mTxtEmpty.setVisibility(View.VISIBLE);
            lvStatistical.setVisibility(View.GONE);
        } else {
            mTxtEmpty.setVisibility(View.GONE);
            lvStatistical.setVisibility(View.VISIBLE);
            adapter.updateList(filterList);
        }
    }


    public List<Schedule> getStatistical() {
        List<Schedule> scheduleList = myDatabase.getAllScheduleByUser(userId);
        List<Schedule> pastSchedule = new ArrayList<>();

        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date currentDate = new Date();
        for (Schedule schedule : scheduleList) {
            try {
                String dateTime = schedule.getDate() + " " + schedule.getTime();
                Date date = input.parse(dateTime);
                if (date != null && date.before(currentDate)) {
                    pastSchedule.add(schedule);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (pastSchedule.isEmpty()) {
            mTxtEmpty.setVisibility(View.VISIBLE);
        } else {
            mTxtEmpty.setVisibility(View.GONE);
        }
        return pastSchedule;
    }

    private void refreshStatistical() {
        List<Schedule> getRef = getStatistical();
        adapter = new CustomAdapterStatistical(getContext(), getRef, myDatabase);
        lvStatistical.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshStatistical();
    }
}