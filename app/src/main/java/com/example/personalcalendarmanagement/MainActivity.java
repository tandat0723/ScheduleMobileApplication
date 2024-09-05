package com.example.personalcalendarmanagement;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.personalcalendarmanagement.data.MyDatabase;
import com.example.personalcalendarmanagement.data.Schedule;
import com.example.personalcalendarmanagement.fragment.HistoryFragment;
import com.example.personalcalendarmanagement.fragment.HomeFragment;
import com.example.personalcalendarmanagement.fragment.OnScheduleAddedListener;
import com.example.personalcalendarmanagement.fragment.StatisticalFragment;
import com.example.personalcalendarmanagement.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements OnScheduleAddedListener {
    private HomeFragment homeFragment;
    private MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabase = new MyDatabase(this);
        homeFragment = new HomeFragment();

        init();
    }

    private void init() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        loadFragment(homeFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_statistical:
                    selectedFragment = new StatisticalFragment();
                    break;
                case R.id.nav_history:
                    selectedFragment = new HistoryFragment();
                    break;
                case R.id.nav_user:
                    selectedFragment = new UserFragment();
                    break;
            }
            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //
    }


    @Override
    public void onScheduleAdded(Schedule schedule) {
        if (homeFragment != null) {
            homeFragment.updateScheduleList(schedule);
        }
    }


}
