package com.example.tah.models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tah.dao.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;
    private final LiveData<List<Task>> tasksLD;
    private final MutableLiveData<List<Integer>> checkedTasksLD = new MutableLiveData<>();


    public TaskViewModel(Application application){
        super(application);
        repository = new TaskRepository(application);
        tasksLD = repository.getTasksLD();
    }

    public LiveData<List<Task>> getTasksLD(){return tasksLD;}

    public LiveData<List<Integer>> getCheckedTasksLD(){return checkedTasksLD;}

    public void insert(Task task){repository.insert(task);}

    public void delete(Task task){repository.delete(task);}

    public void deleteAll(){repository.deleteAll();}

    public void addSelectedTask(List<Integer> ids){checkedTasksLD.setValue(ids);}
}
