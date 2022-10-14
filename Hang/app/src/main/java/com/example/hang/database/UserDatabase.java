package com.example.hang.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.hang.database.dao.UserDao;
import com.example.hang.database.entities.User;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}