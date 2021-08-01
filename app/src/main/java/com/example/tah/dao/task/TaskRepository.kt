package com.example.tah.dao.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tah.models.Task
import com.example.tah.utilities.State
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {

    val state: MutableLiveData<State> = MutableLiveData()
    private val checkedItemsLD = MutableLiveData<List<Int>>(mutableListOf())

    private val disposable = CompositeDisposable()

    fun add(t: Task) {
        state.value = State.loading()

        disposable.add(taskDao.insert(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.added("Habit added")},
                {state.value = State.error("Error")}))
    }

    fun getAll(): LiveData<List<Task>> {
        return taskDao.getAll()
    }

    fun getById(id: Int?): Single<Task> {
        return taskDao.getById(id)
    }

    fun delete(t: Task) {
        disposable.add(taskDao.delete(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.removed("Task removed")},
                {state.value = State.error("Error")}))
    }

    fun deleteAll() {
        disposable.add(taskDao.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }

    fun deleteSelected() {
        disposable.add(taskDao.deleteSelected(checkedItemsLD.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (checkedItemsLD.value!!.size > 1) state.value = State.removed("Tasks removed")
                    else state.value = State.removed("Task removed")
                },
                {state.value = State.error("Error")}))
    }

    fun update(t: Task) {
        disposable.add(taskDao.update(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.updated("Task updated")},
                {state.value = State.error("Task not updated")}))
    }
}