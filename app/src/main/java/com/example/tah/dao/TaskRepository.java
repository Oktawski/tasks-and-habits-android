package com.example.tah.dao;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tah.dao.TaskDao;
import com.example.tah.dao.TaskDatabase;
import com.example.tah.models.Task;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private MutableLiveData<List<Task>> tasksLD;

    public TaskRepository(Application application){
        TaskDatabase db = TaskDatabase.getInstance(application);
        taskDao = db.taskDao();
        tasksLD.setValue(taskDao.getAll());
    }

    public LiveData<List<Task>> getAllTasks(){
        return tasksLD;
    }

    public void insert(Task task){
        TaskDatabase.databaseWriteExecutor.execute(() -> {
            taskDao.insert(task);
        });
    }
}
