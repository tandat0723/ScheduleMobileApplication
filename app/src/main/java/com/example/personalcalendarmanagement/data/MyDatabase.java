package com.example.personalcalendarmanagement.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase {
    private DBHelper helper;
    private SQLiteDatabase db;

    public MyDatabase(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public long addUser(User user) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_USER_NAME, user.getName());
        values.put(DBHelper.COLUMN_USER_USERNAME, user.getUsername());
        values.put(DBHelper.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(DBHelper.COLUMN_USER_ROLE, user.getRole_id().getRole_id());

        return db.insert(DBHelper.TABLE_USER, null, values);
    }

    public void updateUser(User user) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_USER_NAME, user.getName());
        values.put(DBHelper.COLUMN_USER_USERNAME, user.getUsername());
        values.put(DBHelper.COLUMN_USER_PASSWORD, user.getPassword());
        values.put(DBHelper.COLUMN_USER_ROLE, user.getRole_id().getRole_id());

        db.update(DBHelper.TABLE_USER, values, DBHelper.COLUMN_USER_ID + " = ?", new String[]{String.valueOf(user.getUser_id())});
        db.close();
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        User user = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_USER + " WHERE " + DBHelper.COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_ID));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_NAME));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_PASSWORD));
                int roleId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_ROLE));

                Roles roles = Roles.fromInt(roleId);
                user = new User(id, roles, username, password, fullName);
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return user;

    }

    public List<User> getAllUser() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<User> list = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLE_USER, new String[]{"user_id", "username", "name", "password", "role_id"}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_NAME));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_PASSWORD));
                int role = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_ROLE));

                Roles roles = Roles.fromInt(role);

                User user = new User(id, roles, username, password, name);
                list.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = helper.getReadableDatabase();
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

    public void deleteUser(int userId) {
        db.delete(DBHelper.TABLE_USER, DBHelper.COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
    }

    public Cursor checkLogin(String username, String password) {
        db = helper.getReadableDatabase();
        String query = "SELECT * FROM " + DBHelper.TABLE_USER + " WHERE " + DBHelper.COLUMN_USER_USERNAME + " = ?" + " AND " + DBHelper.COLUMN_USER_PASSWORD + " = ?";
        return db.rawQuery(query, new String[]{username, password});
    }

    public long addSchedule(Schedule schedule) {
        SQLiteDatabase db = helper.getWritableDatabase();
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
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Schedule> scheduleList = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLE_SCHEDULE, new String[]{"schedule_id", "title", "description", "type", "date", "time", "user_id"}, DBHelper.COLUMN_SCHEDULE_USER + " = ?", new String[]{String.valueOf(userId)}, null, null, DBHelper.COLUMN_SCHEDULE_ID + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_DESCRIPTION));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_TYPE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_TIME));
                int user_id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_USER));

                Schedule schedule = new Schedule(id, title, description, type, date, time, user_id);
                scheduleList.add(schedule);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return scheduleList;
    }

    public List<Schedule> getSchedulesByTitle(String keyword, int userId) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM " + DBHelper.TABLE_SCHEDULE + " WHERE title LIKE ? AND user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"%" + keyword + "%", String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Schedule schedule = new Schedule(cursor.getInt(cursor.getColumnIndexOrThrow("schedule_id")), cursor.getString(cursor.getColumnIndexOrThrow("title")), cursor.getString(cursor.getColumnIndexOrThrow("description")), cursor.getString(cursor.getColumnIndexOrThrow("type")), cursor.getString(cursor.getColumnIndexOrThrow("date")), cursor.getString(cursor.getColumnIndexOrThrow("time")), cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                schedules.add(schedule);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return schedules;
    }

    public List<Schedule> filterScheduleByDateTime(String startDate, String endDate, String startTime, String endTime) {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Schedule> filteredList = new ArrayList<>();

        String query = "SELECT * FROM " + DBHelper.TABLE_SCHEDULE + " WHERE " + "date >= ? AND date <= ? AND " + "time >= ? AND time <= ?";

        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate, startTime, endTime});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_TITLE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_DESCRIPTION));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_TYPE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_SCHEDULE_TIME));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_ID));

                Schedule schedule = new Schedule(id, title, description, type, date, time, userId);
                filteredList.add(schedule);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return filteredList;
    }


    public void updateSchedule(Schedule schedule) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_SCHEDULE_TITLE, schedule.getTitle());
        values.put(DBHelper.COLUMN_SCHEDULE_DESCRIPTION, schedule.getDescription());
        values.put(DBHelper.COLUMN_SCHEDULE_DATE, schedule.getDate());
        values.put(DBHelper.COLUMN_SCHEDULE_TYPE, schedule.getType());
        values.put(DBHelper.COLUMN_SCHEDULE_TIME, schedule.getTime());
        values.put(DBHelper.COLUMN_SCHEDULE_USER, schedule.getUser_id());

        db.update(DBHelper.TABLE_SCHEDULE, values, DBHelper.COLUMN_SCHEDULE_ID + " = ?", new String[]{String.valueOf(schedule.getSchedule_id())});
        db.close();
    }

    public void deleteSchedule(int scheduleId) {
        db.delete(DBHelper.TABLE_SCHEDULE, DBHelper.COLUMN_SCHEDULE_ID + " = ?", new String[]{String.valueOf(scheduleId)});
    }
}
