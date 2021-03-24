package com.example.tah.dao;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tah.models.Task;

import java.lang.reflect.Array;
import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> tasksLD;

    public TaskRepository(Application application){
        TaskDatabase db = TaskDatabase.getDatabase(application);
        taskDao = db.taskDao();
        tasksLD = taskDao.getAll();
    }

    public LiveData<List<Task>> getTasksLD(){
        return tasksLD;
    }

    public void add(Task task){
        TaskDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insert(task);
        });
    }

    public void delete(Task task){
        TaskDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.delete(task);
        });
    }

    public void deleteAll(){
        TaskDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.deleteAll();
        });
    }
}
