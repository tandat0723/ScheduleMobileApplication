package com.example.personalcalendarmanagement.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.personalcalendarmanagement.R;
import com.example.personalcalendarmanagement.ScheduleActivity;
import com.example.personalcalendarmanagement.adapter.CustomAdapterSchedule;
import com.example.personalcalendarmanagement.data.MyDatabase;
import com.example.personalcalendarmanagement.data.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FloatingActionButton mBtnAddButton;
    private ListView mLvSchedule;
    private SearchView msearchView;
    private SwipeRefreshLayout mswipeRefreshLayout;
    private View mDimBackground;
    private FrameLayout mSearchContainer;
    private boolean isSearch = false;
    private CustomAdapterSchedule adapter;
    private List<Schedule> list;
    private MyDatabase myDatabase;
    private int userId;

    public interface OnScheduleAddedListener {
        void onScheduleAdded(Schedule schedule);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_home_fragment, container, false);

        mBtnAddButton = root.findViewById(R.id.btnAddSchedule);
        msearchView = root.findViewById(R.id.searchView);
        mLvSchedule = root.findViewById(R.id.lvSchedule);
        mswipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        mDimBackground = root.findViewById(R.id.dimBackground);
        mSearchContainer = root.findViewById(R.id.searchContainer);

        list = new ArrayList<>();
        myDatabase = new MyDatabase(getActivity());
        adapter = new CustomAdapterSchedule(getActivity(), R.layout.lv_item_add_schedule, list);
        mLvSchedule.setAdapter(adapter);

//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
//        userId = sharedPreferences.getInt("user_id", -1);
//        if (userId != -1) {
        loadSchedule();
//        } else {
//            Toast.makeText(getActivity(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
//        }

        init();
        return root;
    }

    private void init() {
        mBtnAddButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ScheduleActivity.class);
            startActivity(intent);
        });

        mSearchContainer.setAlpha(0f);
        mDimBackground.setAlpha(0f);

        mswipeRefreshLayout.setOnRefreshListener(() -> {
            if (mSearchContainer.getVisibility() == View.GONE) {
                mSearchContainer.setVisibility(View.VISIBLE);
                mDimBackground.setVisibility(View.VISIBLE);

                mSearchContainer.animate().alpha(1f).setDuration(500).setListener(null);
                mDimBackground.animate().alpha(0.3f).setDuration(500).setListener(null);

                msearchView.requestFocus();
                msearchView.setIconified(false);
            }
            mswipeRefreshLayout.postDelayed(() -> mswipeRefreshLayout.setRefreshing(false), 500);
        });

        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!isSearch) {
                    isSearch = true;

                    Toast.makeText(getContext(), "Tìm kiếm " + s, Toast.LENGTH_SHORT).show();

                    mSearchContainer.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mSearchContainer.setVisibility(View.GONE);
                            isSearch = false;
                        }
                    });

                    mDimBackground.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mDimBackground.setVisibility(View.GONE);
                        }
                    });
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mDimBackground.setOnClickListener(view -> {
            mSearchContainer.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSearchContainer.setVisibility(View.GONE);
                }
            });

            mDimBackground.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mDimBackground.setVisibility(View.GONE);
                }
            });
        });
    }

    private void loadSchedule() {
//        list = myDatabase.getAllScheduleByUser(userId);
//        adapter = new CustomAdapterSchedule(getContext(), R.layout.lv_item_add_schedule, list);
//        mLvSchedule.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        userId = getCurrentUserId();
        list.clear();
        list.addAll(myDatabase.getAllScheduleByUser(userId));
        adapter.notifyDataSetChanged();
    }

    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    public void addSchedule(Schedule schedule) {
        list.add(schedule);
        adapter.notifyDataSetChanged();
    }
}
