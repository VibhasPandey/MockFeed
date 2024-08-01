package com.prasoon.mockfeed;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    //Method to retrieve list of all registered users
    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    //Method to register a user in the database
    @Insert
    void insertUser(User user);

    //More methods such as update() and delele() will be added in future versions
}
