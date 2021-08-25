package com.example.tah.dao.todo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tah.models.Todo
import com.example.tah.utilities.PropertiesTrimmer
import com.example.tah.utilities.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) : PropertiesTrimmer {

    val state = MutableLiveData<State>()
    val completedTodos = MutableLiveData<List<Todo>>(mutableListOf())

    private val disposable = CompositeDisposable()

    fun getAll(id: Int): LiveData<List<Todo>> {
        return todoDao.getAllByTaskId(id)
    }

    fun add(t: Todo) {
        state.value = State.loading()

        trimLeadingAndTrailingWhitespaces(t)

        CoroutineScope(Dispatchers.Main).launch {
            todoDao.insert(t)
            state.value = State.added()
        }
    }

    fun delete(t: Todo) {
        disposable.add(todoDao.delete(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ getAll(t.taskId!!) },{}))
    }

    fun deleteAll() {
        TODO("Not yet implemented")
    }

    fun getCompletedList(): LiveData<List<Todo>> {
        disposable.add(todoDao.getCompletedList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {completedTodos.value = it},
                {}))

        return completedTodos
    }


    fun deleteSelected() {
        disposable.add(todoDao.deleteCompleted()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ getCompletedList() },{}))
    }

    fun update(t: Todo) {
        trimLeadingAndTrailingWhitespaces(t)

        disposable.add(todoDao.update(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    state.value = State.updated("Updated")
                    getCompletedList()
                },
                {
                    state.value = State.error("Could not update")
                    Log.i("TODOREPO", "update: error")
                }))
    }
}