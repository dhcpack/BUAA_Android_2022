package com.example.hang.cl;

public class User {
    public int id;
    public String name;

    public User() {

    }

    public User(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id=" + id;
    }
}
