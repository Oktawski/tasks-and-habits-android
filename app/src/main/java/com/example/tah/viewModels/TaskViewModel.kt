package com.example.tah.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tah.dao.task.TaskRepository
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import com.example.tah.models.TaskWithTodos
import com.example.tah.utilities.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : BaseViewModel<Task>()
{
    private val checkBoxVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)

    init {
        itemsLD =  repository.getAll()
        checkedItemsLD = repository.checkedItemsLD
    }

    fun setCheckBoxVisibility(visibility: Int){
        checkBoxVisibility.value = visibility
    }

    fun getCheckBoxVisibility(): LiveData<Int>{
        return checkBoxVisibility
    }

    fun getAll() {
        itemsLD = repository.getAll()
    }

    fun getFiltered(type: TaskType) {
        itemsLD = repository.getFiltered(type)
    }

    override fun add(t: Task) {
        state.value = State.loading()
        viewModelScope.launch {
            repository.add(t)
        }
        state.value = State.added("Task added")
    }

    suspend fun addTaskWithTodos(taskWithTodos: TaskWithTodos): Long =
        repository.addTaskWithTodos(taskWithTodos)

    suspend fun getTaskById(id: Long): Task {
        return repository.getTaskById(id)
    }

    override fun delete(t: Task) {
        state.value = State.loading()
        viewModelScope.launch {
            repository.delete(t)
            state.value = State.removed("Task removed")
        }
    }

    override fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    override fun deleteSelected() {
        viewModelScope.launch {
            repository.deleteSelected()
        }
    }

    override fun update(t: Task) {
        state.value = State.loading()
        viewModelScope.launch {
            repository.update(t)
        }
        state.value = State.updated("Task updated")
    }

}
