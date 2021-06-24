package com.example.tah.dao.todo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tah.models.Todo;

@Database(entities = {Todo.class}, version = 2, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {

    public abstract TodoDao todoDao();

    private static volatile TodoDatabase INSTANCE;

    public static TodoDatabase getInstance(final Context context){
            if(INSTANCE == null){
                synchronized (TodoDatabase.class){
                    if(INSTANCE == null){
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                TodoDatabase.class, "todo_database")
                                .fallbackToDestructiveMigration()
                                .build();
                    }
                }
            }
            return INSTANCE;
    }
}
