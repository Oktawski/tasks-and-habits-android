package com.example.tah.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tah.dao.todo.TodoRepository
import com.example.tah.models.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository
) : BaseViewModel<Todo>()
{
    val completedTodos: LiveData<List<Todo>>

    init{
        state = repository.state
        completedTodos = repository.completedTodos
    }

    fun getAllByTaskId(id: Int) {
        itemsLD = repository.getAll(id)
    }

    fun getCompletedList(): LiveData<List<Todo>> {
        return repository.getCompletedList()
    }

    override fun add(t: Todo) {
        repository.add(t)
    }

    override fun delete(t: Todo) {
        repository.delete(t)
    }

    override fun deleteAll() {
        repository.deleteAll()
    }

    override fun deleteSelected() {
        repository.deleteSelected()
    }

    override fun update(t: Todo) {
        repository.update(t)
    }

}
