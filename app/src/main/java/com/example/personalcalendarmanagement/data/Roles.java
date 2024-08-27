package com.example.personalcalendarmanagement.data;

public enum Roles {
    ADMIN(1),
    USER(2),
    SUPERUSER(3);

    private int role_id;

    Roles(int roleId) {
        this.role_id = roleId;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
}
