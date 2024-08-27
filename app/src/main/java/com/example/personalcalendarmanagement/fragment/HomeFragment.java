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
    private FloatingActionButton mBtnAddButton;
    private ListView mLvSchedule;
    private SearchView msearchView;
    private SwipeRefreshLayout mswipeRefreshLayout;
    private View mDimBackground;
    private FrameLayout mSearchContainer;
    private boolean isSearch = false;

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

                // Set hieu ung va do trong suot
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



}
