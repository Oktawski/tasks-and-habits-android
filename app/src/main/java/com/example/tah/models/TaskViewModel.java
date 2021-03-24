package com.example.tah.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tah.dao.TaskRepository;

import java.util.List;

public class TaskViewModel extends BaseViewModel<Task>{

    private TaskRepository repository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        itemsLD = repository.getTasksLD();
    }

    @Override
    public void add(Task task) {
        repository.add(task);
    }

    @Override
    public void delete(Task task) {
        repository.delete(task);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
