package com.example.nowastenohunger.Class;

public class UpdatedName {

    private static String username;

    public UpdatedName() {
    }

    public UpdatedName(String username) {
        this.username = username;
    }

    public static String getUsername() {
        return username;
    }

    public  void setUsername(String username) {
        this.username = username;
    }
}
