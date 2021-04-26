package com.example.tah.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tah.models.Todo;

@Database(entities = {Todo.class}, version = 1, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {

    public abstract TodoDao todoDao();

    private static volatile TodoDatabase INSTANCE;

    static TodoDatabase getInstance(final Context context){
            if(INSTANCE == null){
                synchronized (TodoDatabase.class){
                    if(INSTANCE == null){
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                TodoDatabase.class, "todo_database")
                                .build();
                    }
                }
            }
            return INSTANCE;
    }
}
