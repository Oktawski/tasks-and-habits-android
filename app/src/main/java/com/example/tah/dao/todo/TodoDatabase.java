package com.example.tah.dao.todo;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tah.models.Todo;

@Database(entities = {Todo.class}, version = 2, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();
}
