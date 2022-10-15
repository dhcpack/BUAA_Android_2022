package com.example.hang.database.sqlite;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.hang.database.sqlite.entities.User;
import com.example.hang.database.sqlite.dao.UserDao;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}