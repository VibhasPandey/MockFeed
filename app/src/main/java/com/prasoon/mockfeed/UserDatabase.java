package com.prasoon.mockfeed;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, exportSchema = false, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    private static final String DB_NAME = "user_db";
    private static UserDatabase INSTANCE;

    public static synchronized UserDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }

    public abstract UserDao userDao();
}
