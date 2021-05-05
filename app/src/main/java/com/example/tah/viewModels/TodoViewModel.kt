package com.example.tah.viewModels

import android.app.Application
import androidx.annotation.NonNull
import com.example.tah.dao.todo.TodoRepository
import com.example.tah.models.Todo

class TodoViewModel(@NonNull application: Application)
    :BaseViewModel<Todo>(application) {

    private var repository: TodoRepository = TodoRepository(application)

    init{
        itemsLD = repository.getTodosLD()
        state = repository.getState()
    }

    override fun add(t: Todo) {
        repository.add(t)
    }

    override fun delete(t: Todo) {
        repository.delete(t)
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun deleteSelected() {
        repository.deleteCompleted()
    }

    override fun update(t: Todo) {
        repository.update(t)
    }
}