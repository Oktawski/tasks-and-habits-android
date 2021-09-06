package com.example.tah.dao.task;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tah.models.Task;
import com.example.tah.models.Todo;


@Database(entities = {Task.class, Todo.class}, version = 7, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}