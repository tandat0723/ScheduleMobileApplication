package com.example.personalcalendarmanagement.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.personalcalendarmanagement.R;
import com.example.personalcalendarmanagement.adapter.CustomAdapterScheduleHistory;
import com.example.personalcalendarmanagement.data.MyDatabase;
import com.example.personalcalendarmanagement.data.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryFragment extends Fragment {
    private TextView mTxtEmpty;
    private ListView mLvHistory;
    private MyDatabase myDatabase;
    private CustomAdapterScheduleHistory adapter;
    private List<Schedule> list;
    private int userId;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_history_fragment, container, false);

        mLvHistory = root.findViewById(R.id.lvHistory);
        mTxtEmpty = root.findViewById(R.id.txtEmpty);

        list = new ArrayList<>();
        myDatabase = new MyDatabase(getActivity());
        adapter = new CustomAdapterScheduleHistory(getActivity(), list, myDatabase);

        loadSchedule();

        return root;
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


    public List<Schedule> getScheduleHistory() {
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

    private void refreshScheduleHistory() {
        List<Schedule> getRef = getScheduleHistory();
        adapter = new CustomAdapterScheduleHistory(getContext(), getRef, myDatabase);
        mLvHistory.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshScheduleHistory();
    }
}