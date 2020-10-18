package com.example.smartageliketool.data.sqlite;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.smartageliketool.data.model.post.PostDataBaseEntity;
import com.example.smartageliketool.data.model.post.PostTableDao;


@Database(entities = PostDataBaseEntity.class,exportSchema = false,version = 1)
public abstract class DatabaseModule extends RoomDatabase {

    private static final String DB_NAME = "like_tool_db";
    private static DatabaseModule instance;

    public static synchronized DatabaseModule getInstance(Context context) {
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),DatabaseModule.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract PostTableDao postTableDao();
}
