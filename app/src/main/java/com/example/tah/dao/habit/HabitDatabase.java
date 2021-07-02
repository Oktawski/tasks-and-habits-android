package com.example.tah.dao.habit;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tah.models.Habit;


@Database(entities = {Habit.class}, version = 3, exportSchema = false )
public abstract class HabitDatabase extends RoomDatabase {

    public abstract HabitDao habitDao();

    private static volatile HabitDatabase INSTANCE;

    public static HabitDatabase getDatabase (final Context context){
        if(INSTANCE == null){
            synchronized(HabitDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        HabitDatabase.class, "habit_database")
                            .fallbackToDestructiveMigration()
                        .build();
                }
            }
        }
        return INSTANCE;
    }
}
