package com.example.tah.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tah.dao.todo.TodoRepository
import com.example.tah.models.Todo
import com.example.tah.utilities.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.net.ssl.SSLEngineResult

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : BaseViewModel<Todo>()
{
    fun getTodosByTaskId(taskId: Long): LiveData<List<Todo>> {
        itemsLD = repository.getAllByTaskId(taskId)
        return itemsLD!!
    }

    fun getCompletedByTaskId(taskId: Long) = repository.getCompletedByTaskId(taskId)

    fun deleteCompletedByTaskId(taskId: Long) {
        state.value = State.loading()
        viewModelScope.launch {
            repository.deleteCompletedByTaskId(taskId)
            state.value = State.removed("Tasks removed")
        }
    }

    override fun add(t: Todo) {
        state.value = State.loading()
        viewModelScope.launch {
            repository.add(t)
            state.value = State.added("")
        }
    }

    override fun update(t: Todo) {
        state.value = State.loading()
        viewModelScope.launch {
            repository.update(t)
            state.value = State.updated("Todo updated")
        }
    }

    override fun delete(t: Todo) {
        viewModelScope.launch {
            repository.delete(t)
        }
    }

    override fun deleteAll() {
        repository.deleteAll()
    }

    override fun deleteSelected() {
    }
}
