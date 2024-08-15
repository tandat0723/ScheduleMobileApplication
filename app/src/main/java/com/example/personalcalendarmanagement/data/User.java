package com.example.personalcalendarmanagement.data;

public class User {
    private int user_id;
    private String username;
    private String name;
    private String password;
    private Roles role_id;

    public User(int user_id, Roles role_id, String username, String password) {
        this.user_id = user_id;
        this.role_id = role_id;
        this.username = username;
        this.password = password;
    }

    public User(int user_id, Roles role_id, String username, String password, String name) {
        this.user_id = user_id;
        this.role_id = role_id;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole_id() {
        return role_id;
    }

    public void setRole_id(Roles role_id) {
        this.role_id = role_id;
    }
}
