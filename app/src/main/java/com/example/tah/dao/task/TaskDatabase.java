package com.example.tah.dao.task;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tah.models.Task;


@Database(entities = {Task.class}, version = 3, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}