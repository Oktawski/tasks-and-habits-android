package com.example.tah.dao.habit;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tah.models.Habit;


@Database(entities = {Habit.class}, version = 3, exportSchema = false )
public abstract class HabitDatabase extends RoomDatabase {
    public abstract HabitDao habitDao();
}
