package com.prasoon.mockfeed;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "username")
    private final String username;

    @ColumnInfo(name = "password")
    private final String password;

    User(int id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Ignore
    User(String username, String password){
        this.username = username;
        this.password = password;
    }

    int getId(){
        return id;
    }

    String getUsername(){
        return username;
    }

    String getPassword(){
        return password;
    }
}
