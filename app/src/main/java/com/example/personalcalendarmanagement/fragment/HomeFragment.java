package com.example.personalcalendarmanagement.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {
    private FloatingActionButton mbtnAddButton;
    private ListView mlvSchedule;
    private SearchView msearchView;
    private SwipeRefreshLayout mswipeRefreshLayout;
    private View mdimBackground;
    private FrameLayout msearchContainer;
    private boolean isSearch = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_home_fragment, container, false);

        mbtnAddButton = root.findViewById(R.id.btnAddSchedule);
        msearchView = root.findViewById(R.id.searchView);
        mlvSchedule = root.findViewById(R.id.lvSchedule);
        mswipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        mdimBackground = root.findViewById(R.id.dimBackground);
        msearchContainer = root.findViewById(R.id.searchContainer);

        init();
        return root;
    }

    private void init() {
        mbtnAddButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ScheduleActivity.class);
            startActivity(intent);
        });

        msearchContainer.setAlpha(0f);
        mdimBackground.setAlpha(0f);

        mswipeRefreshLayout.setOnRefreshListener(() -> {
            if (msearchContainer.getVisibility() == View.GONE) {
                msearchContainer.setVisibility(View.VISIBLE);
                mdimBackground.setVisibility(View.VISIBLE);

                // Set hieu ung va do trong suot
                msearchContainer.animate().alpha(1f).setDuration(500).setListener(null);
                mdimBackground.animate().alpha(0.3f).setDuration(500).setListener(null);

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

                    msearchContainer.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            msearchContainer.setVisibility(View.GONE);
                            isSearch = false;
                        }
                    });

                    mdimBackground.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mdimBackground.setVisibility(View.GONE);
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

        mdimBackground.setOnClickListener(view -> {
            msearchContainer.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    msearchContainer.setVisibility(View.GONE);
                }
            });

            mdimBackground.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mdimBackground.setVisibility(View.GONE);
                }
            });
        });
    }
}
