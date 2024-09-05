package com.example.personalcalendarmanagement.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personalcalendarmanagement.R;
import com.example.personalcalendarmanagement.ScheduleActivity;
import com.example.personalcalendarmanagement.adapter.CustomAdapterSchedule;
import com.example.personalcalendarmanagement.data.MyDatabase;
import com.example.personalcalendarmanagement.data.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private FloatingActionButton mBtnAddButton;
    private ListView mLvSchedule;
    private CustomAdapterSchedule adapter;
    private List<Schedule> list;
    private MyDatabase myDatabase;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_home_fragment, container, false);

        mBtnAddButton = root.findViewById(R.id.btnAddSchedule);
        mLvSchedule = root.findViewById(R.id.lvSchedule);

        list = new ArrayList<>();
        myDatabase = new MyDatabase(getContext());
        adapter = new CustomAdapterSchedule(getActivity(), list, myDatabase);
        mLvSchedule.setAdapter(adapter);

        loadSchedule();

        init();
        return root;
    }

    private void init() {
        mBtnAddButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ScheduleActivity.class);
            startActivity(intent);
        });

    }


    public void updateScheduleList(Schedule schedule) {
        list.add(schedule);
        adapter.notifyDataSetChanged();
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

    private List<Schedule> getUpdateSchedule() {
        List<Schedule> scheduleList = myDatabase.getAllScheduleByUser(userId);
        List<Schedule> updateSchedule = new ArrayList<>();

        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date currentDate = new Date();
        for (Schedule schedule : scheduleList) {
            try {
                String dateTime = schedule.getDate() + " " + schedule.getTime();
                Date date = input.parse(dateTime);
                if (date != null && date.after(currentDate)) {
                    updateSchedule.add(schedule);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return updateSchedule;
    }

    public void refreshListView() {
        List<Schedule> getUpdate = getUpdateSchedule();
        adapter = new CustomAdapterSchedule(getContext(), getUpdate, myDatabase);
        mLvSchedule.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshListView();
    }


}
