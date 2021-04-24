package com.example.tah.viewModels

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import com.example.tah.dao.TodoRepository
import com.example.tah.models.Todo
import com.example.tah.utilities.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

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