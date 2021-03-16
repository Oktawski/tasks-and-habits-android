package com.example.tah.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tah.dao.TaskRepository;
import com.example.tah.models.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;
    private final LiveData<List<Task>> tasksLD;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        tasksLD = repository.getAllTasks();
    }

    public LiveData<List<Task>> getTasksLD(){
        return tasksLD;
    }

    public void insert(Task task){
        repository.insert(task);
    }




}
