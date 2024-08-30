package com.example.personalcalendarmanagement.data;

public class Schedule {
    private int schedule_id;
    private String title;
    private String description;
    private String type;
    private String date;
    private String time;
    private int user_id;

    public Schedule() {
    }

    public Schedule(String title, String description, String type, String date, String time, int user_id) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.type = type;
        this.user_id = user_id;
    }

    public Schedule(int schedule_id, String title, String description, String type, String date, String time, int user_id) {
        this.schedule_id = schedule_id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.type = type;
        this.user_id = user_id;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
