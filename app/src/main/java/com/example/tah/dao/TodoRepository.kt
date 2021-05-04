package com.example.tah.dao

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tah.models.Todo
import com.example.tah.utilities.State
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TodoRepository(application: Application) {

    private var todoDao: TodoDao
    private var todosLD: LiveData<List<Todo>>
    private var state: MutableLiveData<State> = MutableLiveData()

    private val disposable = CompositeDisposable()

    init {
        val database = TodoDatabase.getInstance(application)
        todoDao = database.todoDao()
        todosLD = todoDao.getAll()
    }

    fun getState(): MutableLiveData<State> = state

    fun getTodosLD(): LiveData<List<Todo>> = todosLD

    fun add(todo: Todo){
        state.value = State.loading()

        disposable.add(todoDao.insert(todo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({state.value = State.success("Todo added")},
                        {state.value = State.error("Error")}))
    }

    fun getById(id: Int): Single<Todo> {
        return todoDao.getById(id)
    }

    fun delete(todo: Todo){
        disposable.add(todoDao.delete(todo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }

    fun deleteCompleted(){
        disposable.add(todoDao.deleteCompleted()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
    }

    fun update(todo: Todo){
        disposable.add(todoDao.update(todo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({//state.value = State.success("henlo frens")
                    Log.i("TODOREPO", "update: ok")
                },
                        {
                            //state.value = State.error("unhello")
                            Log.i("TODOREPO", "update: error")
                        }))
    }
}