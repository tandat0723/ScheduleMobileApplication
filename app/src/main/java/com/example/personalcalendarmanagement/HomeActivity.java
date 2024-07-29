package com.example.personalcalendarmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    private ImageButton mbtnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init() {
        mbtnMenu = findViewById(R.id.btnMenu);

        mbtnMenu.setOnClickListener(this::PopupMenu);
    }

    private void PopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_add:
//                    Toast.makeText(HomeActivity.this, "Them", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, ScheduleActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.menu_statistics:
                    Toast.makeText(HomeActivity.this, "Thong ke", Toast.LENGTH_SHORT).show();
                    Intent intent_statistics = new Intent(HomeActivity.this, StatisticalActivity.class);
                    startActivity(intent_statistics);
                    return true;
                case R.id.menu_history:
//                    Toast.makeText(HomeActivity.this, "lich su", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(HomeActivity.this, HistoryActivity.class);
                    startActivity(i);
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }
}