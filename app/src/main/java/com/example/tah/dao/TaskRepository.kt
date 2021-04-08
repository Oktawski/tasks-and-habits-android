package com.example.tah.dao

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tah.models.Task
import com.example.tah.utilities.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TaskRepository(application: Application) {

    private var taskDao: TaskDao
    private var tasksLD: LiveData<List<Task>>
    private var state: MutableLiveData<State> = MutableLiveData()

    private val disposable = CompositeDisposable()

    init{
        val database: TaskDatabase = TaskDatabase.getDatabase(application)
        taskDao = database.taskDao()
        tasksLD = taskDao.getAll()
    }

    fun getState(): MutableLiveData<State> = state

    fun getTasksLD(): LiveData<List<Task>>{
        return tasksLD
    }

    fun add(task: Task){
        state.value = State.loading()

        disposable.add(taskDao.insert(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({state.value = State.success("Task added")},
                        {state.value = State.error("Error")}))

    }

    fun delete(task: Task){
        TaskDatabase.databaseWriteExecutor.execute { taskDao.delete(task) }
    }

    fun deleteSelected(idList: List<Int>){
        TaskDatabase.databaseWriteExecutor.execute { taskDao.deleteSelected(idList) }
    }

    fun deleteAll(){
        TaskDatabase.databaseWriteExecutor.execute { taskDao.deleteAll() }
    }
}