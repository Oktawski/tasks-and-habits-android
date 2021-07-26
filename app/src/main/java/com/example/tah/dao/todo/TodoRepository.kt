package com.example.tah.dao.todo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tah.models.Todo
import com.example.tah.utilities.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) {
    val state = MutableLiveData<State>()

    private val disposable = CompositeDisposable()

    fun getAll(): LiveData<List<Todo>> {
        return todoDao.getAll()
    }

    fun add(t: Todo) {
        state.value = State.loading()

        disposable.add(todoDao.insert(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.added()},
                {state.value = State.error("Error")}))

    }

    fun delete(t: Todo) {
        TODO("Not yet implemented")
    }

    fun deleteAll() {
        TODO("Not yet implemented")
    }

    fun deleteSelected() {
        disposable.add(todoDao.deleteCompleted()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }

    fun update(t: Todo) {
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