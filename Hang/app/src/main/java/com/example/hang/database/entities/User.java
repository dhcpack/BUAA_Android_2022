package com.example.hang.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "nick_name")
    public String nickName;

    @ColumnInfo(name = "password")
    public String password;

    public User(String nickName, String password) {
        this.nickName = nickName;
        this.password = password;
    }
}
