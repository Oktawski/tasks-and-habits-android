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
import kotlinx.coroutines.*
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) : PropertiesTrimmer {

    val state = MutableLiveData<State>()
    private val disposable = CompositeDisposable()

    fun getAll(id: Long): LiveData<List<Todo>> {
        return todoDao.getAllByTaskId(id)
    }

    suspend fun getTodosByTaskId(id: Long) = todoDao.getTodosByTaskId(id)

    suspend fun add(t: Todo): Long {
        state.value = State.loading()
        trimLeadingAndTrailingWhitespaces(t)

        var id = -1L

        CoroutineScope(Dispatchers.Main).launch {
            id = todoDao.insert(t)
            state.value = State.added()
        }

        return id
    }

    suspend fun delete(t: Todo) {
        todoDao.delete(t)
    }

    fun deleteCompletedByTaskId(taskId: Long) {
        disposable.add(todoDao.deleteCompletedByTaskId(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {getAll(taskId)}, {}))
    }

    fun deleteAll() {
        disposable.add(todoDao.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }

    fun getCompletedByTaskId(taskId: Long): LiveData<List<Todo>> {
        return todoDao.getCompletedByTaskId(taskId)
    }

    fun update(t: Todo) {
        trimLeadingAndTrailingWhitespaces(t)

        disposable.add(todoDao.update(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    state.value = State.updated("Updated")
                    getCompletedByTaskId(t.taskId!!)
                },
                {
                    state.value = State.error("Could not update")
                    Log.i("TODOREPO", "update: error")
                }))
    }

    fun deleteTodosByTaskId(taskId: Long) {
        disposable.add(todoDao.deleteTodosByTaskId(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }
}