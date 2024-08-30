package com.example.personalcalendarmanagement.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personalcalendarmanagement.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase {
    private DBHelper helper;
    private SQLiteDatabase db;

    public MyDatabase(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public boolean authenticateUser(String username, String password) {
        String hashPassword = Utils.hashPassword(password);
        String query = "SELECT COUNT(*) FROM " + DBHelper.TABLE_USER + " WHERE " + DBHelper.COLUMN_USER_USERNAME + " =?" + " AND " + DBHelper.COLUMN_USER_PASSWORD + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{username, hashPassword});
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        return false;
    }

    public long addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_USER_NAME, user.getName());
        values.put(DBHelper.COLUMN_USER_USERNAME, user.getUsername());
        values.put(DBHelper.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(DBHelper.COLUMN_USER_ROLE, user.getRole_id().getRole_id());

        return db.insert(DBHelper.TABLE_USER, null, values);
    }

    public boolean userExists(String username) {
        String query = "SELECT COUNT(*) FROM " + DBHelper.TABLE_USER + " WHERE " + DBHelper.COLUMN_USER_USERNAME + " =?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        return false;
    }

    public Cursor checkLogin(String username, String password) {
        db = helper.getReadableDatabase();
        String query = "SELECT * FROM " + DBHelper.TABLE_USER +
                " WHERE " + DBHelper.COLUMN_USER_USERNAME + " =?" + " AND " + DBHelper.COLUMN_USER_PASSWORD + " =?";
        return db.rawQuery(query, new String[]{username, password});
    }

    public long addSchedule(Schedule schedule) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SCHEDULE_TITLE, schedule.getTitle());
        values.put(DBHelper.COLUMN_SCHEDULE_DESCRIPTION, schedule.getDescription());
        values.put(DBHelper.COLUMN_SCHEDULE_DATE, schedule.getDate());
        values.put(DBHelper.COLUMN_SCHEDULE_TYPE, schedule.getType());
        values.put(DBHelper.COLUMN_SCHEDULE_TIME, schedule.getTime());
        values.put(DBHelper.COLUMN_SCHEDULE_USER, schedule.getUser_id());

        return db.insert(DBHelper.TABLE_SCHEDULE, null, values);
    }

    public List<Schedule> getAllScheduleByUser(int userId) {
        List<Schedule> scheduleList = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLE_SCHEDULE,
                new String[]{"schedule_id", "title", "description", "type", "date", "time", "user_id"},
                DBHelper.COLUMN_SCHEDULE_USER + " =?",
                new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("schedule_id"));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex("type"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
                @SuppressLint("Range") int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));

                Schedule schedule = new Schedule(id, title, description, type, date, time, user_id);
                scheduleList.add(schedule);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return scheduleList;
    }
}
