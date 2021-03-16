package com.example.tah.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tah.models.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

    private static volatile TaskDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TaskDatabase getInstance(final Context context){
        if(instance == null) {
            synchronized (TaskDatabase.class) {
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "task_database")
                            .build();
                }
            }
        }
        return instance;
    }
}
