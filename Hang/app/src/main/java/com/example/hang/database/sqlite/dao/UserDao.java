package com.example.hang.database.sqlite.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hang.database.sqlite.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE nick_name=:nickName")
    List<User> loadAllByIds(String nickName);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}