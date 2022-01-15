package com.example.tah.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tah.dao.task.TaskRepository
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import com.example.tah.models.TaskWithTodos
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
        state = repository.state
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

    override suspend fun add(t: Task) = repository.add(t)

    //suspend fun addGetId(t: Task): Long = repository.add(t)

    //suspend fun getTaskWithTodosByTaskId(id: Int) = repository.getTaskWithTodosByTaskId(id)

    suspend fun addTaskWithTodos(taskWithTodos: TaskWithTodos): Long = repository.addTaskWithTodos(taskWithTodos)

    /*fun getById(id: Int?): Single<Task> {
        return repository.getById(id)
    }*/

    suspend fun getTaskById(id: Long): Task {
        return repository.getTaskById(id)
    }

    override fun delete(t: Task) {
        viewModelScope.launch {
            repository.delete(t)
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
        repository.update(t)
    }

}
