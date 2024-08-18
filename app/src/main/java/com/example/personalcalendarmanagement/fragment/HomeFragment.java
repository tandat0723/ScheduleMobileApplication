package com.example.personalcalendarmanagement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.personalcalendarmanagement.R;
import com.example.personalcalendarmanagement.ScheduleActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {
    private FloatingActionButton mbtnAddButton;
    private ListView mlvSchedule;
    private SearchView msearchView;
    private SwipeRefreshLayout mswipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_home_fragment, container, false);

        mbtnAddButton = root.findViewById(R.id.btnAddSchedule);
        msearchView = root.findViewById(R.id.searchView);
        mlvSchedule = root.findViewById(R.id.lvSchedule);
        mswipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);

        init();
        return root;
    }

    private void init() {
        mbtnAddButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ScheduleActivity.class);
            startActivity(intent);
        });

        mswipeRefreshLayout.setOnRefreshListener(() -> {
            if (msearchView.getVisibility() == View.GONE) {
                msearchView.setVisibility(View.VISIBLE);
            }

            mswipeRefreshLayout.postDelayed(() -> mswipeRefreshLayout.setRefreshing(false), 500);
        });

        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}
