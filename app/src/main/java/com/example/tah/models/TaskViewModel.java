package com.example.tah.models;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tah.dao.TaskRepository;


public class TaskViewModel extends BaseViewModel<Task>{

    private TaskRepository repository;

    private final MutableLiveData<Integer> checkBoxVisibility = new MutableLiveData<>(View.GONE);

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        itemsLD = repository.getTasksLD();
    }

    public void setCheckBoxVisibility(Integer visibility){
        checkBoxVisibility.setValue(visibility);
    }

    public LiveData<Integer> getCheckBoxVisibility(){
        return checkBoxVisibility;
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

    @Override
    public void deleteSelected(){
        repository.deleteSelected(getCheckedItemsLD().getValue());
    }

}
