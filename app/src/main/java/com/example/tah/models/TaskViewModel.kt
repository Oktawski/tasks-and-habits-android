package com.example.tah.models

import android.app.Application
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tah.dao.TaskRepository

class TaskViewModel(@NonNull application: Application)
    :BaseViewModel<Task>(application)
{
    private var repository: TaskRepository = TaskRepository(application)
    private val checkBoxVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)

    init {
        itemsLD = repository.getTasksLD()
        state = repository.getState()
    }

    fun setCheckBoxVisibility(visibility: Int){
        checkBoxVisibility.value = visibility
    }

    fun getCheckBoxVisibility(): LiveData<Int>{
        return checkBoxVisibility
    }

    override fun add(t: Task) {
        repository.add(t)
    }

    override fun delete(t: Task) {
        repository.delete(t)
    }

    override fun deleteAll() {
        repository.deleteAll()
    }

    override fun deleteSelected() {
        repository.deleteSelected(checkedItemsLD.value!!)
    }
}