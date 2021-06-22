package com.example.tah.viewModels

import android.app.Application
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tah.dao.task.TaskDao
import com.example.tah.dao.task.TaskDatabase
import com.example.tah.models.Task
import com.example.tah.utilities.State
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TaskViewModel(@NonNull application: Application)
    : BaseViewModel<Task>(application)
{
    private var taskDao: TaskDao
    private val checkBoxVisibility: MutableLiveData<Int> = MutableLiveData(View.GONE)

    private val disposable = CompositeDisposable()

    init {
        val database: TaskDatabase = TaskDatabase.getDatabase(application)
        taskDao = database.taskDao()
        itemsLD = taskDao.getAll()
        state = MutableLiveData()
    }

    fun setCheckBoxVisibility(visibility: Int){
        checkBoxVisibility.value = visibility
    }

    fun getCheckBoxVisibility(): LiveData<Int>{
        return checkBoxVisibility
    }

    override fun add(t: Task) {
        state.value = State.loading()

        disposable.add(taskDao.insert(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.added("Task added")},
                {state.value = State.error("Error")}))
    }

    fun getById(id: Int?): Single<Task> {
        return taskDao.getById(id)
    }

    override fun delete(t: Task) {
        disposable.add(taskDao.delete(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.removed("Task removed")},
                {state.value = State.error("Error")}))
    }

    override fun deleteAll() {
        disposable.add(taskDao.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe())
    }

    override fun deleteSelected() {
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

    override fun update(t: Task) {
        disposable.add(taskDao.update(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.updated("Task updated")},
                {state.value = State.error("Task not updated")}))
    }

}