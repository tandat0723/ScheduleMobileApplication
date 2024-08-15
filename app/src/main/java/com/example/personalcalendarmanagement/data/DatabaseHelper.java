package com.example.personalcalendarmanagement.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final String TABLE_USER = "user";
    private static final String TABLE_ROLES = "roles";
    private static final String TABLE_SCHEDULE = "schedule";

    // Column for table user
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_USERNAME = "username";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_ROLE = "role_id";

    // Column for table roles
    private static final String COLUMN_ROLE_ID = "role_id";
    private static final String COLUMN_ROLE_NAME = "role_name";

    // Column for table schedule
    private static final String COLUMN_SCHEDULE_ID = "schedule_id";
    private static final String COLUMN_SCHEDULE_USER_ID = "user_id";
    private static final String COLUMN_SCHEDULE_TITLE = "title";
    private static final String COLUMN_SCHEDULE_DATE = "date";
    private static final String COLUMN_SCHEDULE_TIME = "time";
    private static final String COLUMN_SCHEDULE_TYPE = "type";
    private static final String COLUMN_SCHEDULE_DESCRIPTION = "description";

    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "(" + COLUMN_USER_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT," + COLUMN_USER_USERNAME +
            " TEXT NOT NULL," + COLUMN_USER_PASSWORD + " TEXT NOT NULL," + COLUMN_USER_ROLE + " INTEGER," +
            " FOREIGN KEY (" + COLUMN_USER_ROLE + ") REFERENCES " + TABLE_ROLES + "(" + COLUMN_ROLE_ID + "))";

    private static final String CREATE_TABLE_ROLES = "CREATE TABLE " + TABLE_ROLES + "(" + COLUMN_ROLE_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ROLE_NAME + " TEXT NOT NULL)";

    private static final String CREATE_TABLE_SCHEDULE = "CREATE TABLE " + TABLE_SCHEDULE + "(" + COLUMN_SCHEDULE_ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_SCHEDULE_USER_ID + " INTEGER," + COLUMN_SCHEDULE_TITLE + " TEXT," +
            COLUMN_SCHEDULE_DESCRIPTION + " TEXT," + COLUMN_SCHEDULE_DATE + " TEXT," + COLUMN_SCHEDULE_TIME + " TEXT," +
            COLUMN_SCHEDULE_TYPE + " TEXT," + " FOREIGN KEY (" + COLUMN_SCHEDULE_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
        sqLiteDatabase.execSQL(CREATE_TABLE_ROLES);
        sqLiteDatabase.execSQL("INSERT INTO roles (role_name) VALUES ('Admin')");
        sqLiteDatabase.execSQL("INSERT INTO roles (role_name) VALUES ('User')");
        sqLiteDatabase.execSQL(CREATE_TABLE_SCHEDULE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);

        onCreate(sqLiteDatabase);
    }

    public long addUser(String username, String password, String name, int roleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("name", name);
        values.put("role_id", roleId);
        return db.insert("user", null, values);
    }

    public Cursor checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * From user Where username = ? AND password = ?";
        return db.rawQuery(query, new String[]{username, password});
    }
}
