package com.example.tah.viewModels

import androidx.lifecycle.viewModelScope
import com.example.tah.dao.todo.TodoRepository
import com.example.tah.models.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : BaseViewModel<Todo>()
{
    init{
        state = repository.state
    }

    fun getAllByTaskId(id: Long) {
        itemsLD = repository.getAll(id)
    }

    suspend fun getTodosByTaskId(id: Long) = repository.getTodosByTaskId(id)

    fun deleteCompletedByTaskId(taskId: Long) = repository.deleteCompletedByTaskId(taskId)

    fun getCompletedByTaskId(taskId: Long) = repository.getCompletedByTaskId(taskId)

    override suspend fun add(t: Todo): Long {
        return repository.add(t)
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

    override fun update(t: Todo) {
        repository.update(t)
    }

}
