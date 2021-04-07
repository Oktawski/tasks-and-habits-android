package com.example.tah.dao

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.tah.models.Task

class TaskRepository(application: Application) {

    private var taskDao: TaskDao
    private var tasksLD: LiveData<List<Task>>

    init{
        val database: TaskDatabase = TaskDatabase.getDatabase(application)
        taskDao = database.taskDao()
        tasksLD = taskDao.getAll()
    }

    fun getTasksLD(): LiveData<List<Task>>{
        return tasksLD
    }

    fun add(task: Task){
        TaskDatabase.databaseWriteExecutor.execute { taskDao.insert(task) }
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