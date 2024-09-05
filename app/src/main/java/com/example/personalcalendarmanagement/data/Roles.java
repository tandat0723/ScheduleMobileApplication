package com.example.personalcalendarmanagement.data;

public enum Roles {
    Administrator(1, "Administrator"), User(2, "User"), Admin(3, "Admin");

    private final int role_id;
    private final String name;

    Roles(int roleId, String name) {
        this.role_id = roleId;
        this.name = name;
    }

    public int getRole_id() {
        return role_id;
    }

    public String getName() {
        return name;
    }

    public static Roles fromInt(int i) {
        for (Roles roles : Roles.values()) {
            if (roles.getRole_id() == i) {
                return roles;
            }
        }
        return null;
    }
}
