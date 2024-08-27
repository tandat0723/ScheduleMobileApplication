package com.example.personalcalendarmanagement.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personalcalendarmanagement.Utils.Utils;
import com.example.personalcalendarmanagement.data.DBHelper;
import com.example.personalcalendarmanagement.data.User;

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
        values.put(DBHelper.COLUMN_SCHEDULE_USER_ID, schedule.getUser_id().getUser_id());

        db.close();
        return db.insert(DBHelper.TABLE_SCHEDULE, null, values);
    }

    public void close() {
        helper.close();
    }
}
