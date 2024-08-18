package com.example.personalcalendarmanagement;

import android.content.Intent;
import android.os.Bundle;;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalcalendarmanagement.data.User;
import com.example.personalcalendarmanagement.item.ItemSchedule;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ImageButton mbtnMenu;
    private ListView mlvSchedule;
    private CustomAdapterSchedule customAdapterSchedule;
    private List<ItemSchedule> itemSchedules;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init() {
        mbtnMenu = findViewById(R.id.btnMenu);
        mlvSchedule = findViewById(R.id.lvSchedule);
        itemSchedules = new ArrayList<>();

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        if (title != null && description != null && date != null && time != null) {
            ItemSchedule item = new ItemSchedule(title, description, date, time);
            itemSchedules.add(item);
        }

        customAdapterSchedule = new CustomAdapterSchedule(this, itemSchedules);
        mlvSchedule.setAdapter(customAdapterSchedule);

        mbtnMenu.setOnClickListener(this::PopupMenu);
    }

    private void PopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_add:
                    Intent intent = new Intent(HomeActivity.this, ScheduleActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.menu_statistics:
                    Intent intent_statistics = new Intent(HomeActivity.this, StatisticalActivity.class);
                    startActivity(intent_statistics);
                    return true;
                case R.id.menu_history:
                    Intent i = new Intent(HomeActivity.this, HistoryActivity.class);
                    startActivity(i);
                    return true;
                case R.id.menu_user:
                    Intent i_user = new Intent(HomeActivity.this, UserActivity.class);
                    startActivity(i_user);
                    return true;
                case R.id.menu_logout:
                    Intent intent_logout = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent_logout);
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

    public void onBackPressed() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}