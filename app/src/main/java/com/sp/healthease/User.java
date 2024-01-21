package com.sp.healthease;

public class User {
    private String name;
    private String email;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

