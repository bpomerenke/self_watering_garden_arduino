package org.autogarden.dto;

import java.util.List;

public class User {
    private String _id;
    private String username;
    private List<String> sensors;

    public User(String username, List<String> sensors) {
        this.username = username;
        this.sensors = sensors;
    }

    public String get_id() {
        return _id;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getSensors() {
        return sensors;
    }
}
