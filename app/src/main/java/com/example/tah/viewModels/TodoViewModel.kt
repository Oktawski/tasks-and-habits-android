package com.example.tah.viewModels

import androidx.lifecycle.LiveData
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
    val disposable = CompositeDisposable()

    init{
        itemsLD = repository.getAll()
        state = repository.state
        completedTodos = repository.completedTodos
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

    fun getCompletedList(): LiveData<List<Todo>> {
        return repository.getCompletedList()
    }

}
