package com.example.tah.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tah.models.Habit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Habit.class}, version = 1, exportSchema = false )
public abstract class HabitDatabase extends RoomDatabase {

    public abstract HabitDao habitDao();

    private static volatile HabitDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 6;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static HabitDatabase getDatabase (final Context context){
        if(INSTANCE == null){
            synchronized(HabitDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        HabitDatabase.class, "word_database")
                        .build();
                }
            }
        }
        return INSTANCE;
    }
}
