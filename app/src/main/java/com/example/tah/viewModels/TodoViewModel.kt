package com.example.tah.viewModels

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import com.example.tah.dao.todo.TodoDao
import com.example.tah.dao.todo.TodoDatabase
import com.example.tah.models.Todo
import com.example.tah.utilities.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TodoViewModel(@NonNull application: Application)
    :BaseViewModel<Todo>(application) {

    private var todoDao: TodoDao

    private val disposable = CompositeDisposable()

    init{
        val database = TodoDatabase.getInstance(application)
        todoDao = database.todoDao()
        itemsLD = todoDao.getAll()
        state = MutableLiveData()
    }

    override fun add(t: Todo) {
        state.value = State.loading()

        disposable.add(todoDao.insert(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.added()},
                {state.value = State.error("Error")}))

    }

    override fun delete(t: Todo) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun deleteSelected() {
        disposable.add(todoDao.deleteCompleted()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }

    override fun update(t: Todo) {
        disposable.add(todoDao.update(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    state.value = State.updated("Updated")
                },
                {
                    state.value = State.error("Could not update")
                    Log.i("TODOREPO", "update: error")
                }))
    }
}