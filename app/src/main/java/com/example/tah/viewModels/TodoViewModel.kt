package com.example.tah.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tah.dao.todo.TodoRepository
import com.example.tah.models.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    fun getAllByTaskId(id: Int) {
        itemsLD = repository.getAll(id)
    }

    suspend fun getTodosByTaskId(id: Int) = repository.getTodosByTaskId(id)

    fun deleteCompletedByTaskId(taskId: Int) = repository.deleteCompletedByTaskId(taskId)

    fun getCompletedByTaskId(taskId: Int) = repository.getCompletedByTaskId(taskId)

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
