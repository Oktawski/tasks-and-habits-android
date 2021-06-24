package com.example.tah.viewModels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import com.example.tah.dao.habit.HabitDao
import com.example.tah.dao.habit.HabitDatabase
import com.example.tah.models.Habit
import com.example.tah.utilities.State
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HabitViewModel(@NonNull application: Application):
    BaseViewModel<Habit>(application)
{
    private var habitDao: HabitDao

    val habitTime = MutableLiveData<Long>()
    val isStarted = MutableLiveData(false)

    private val disposable = CompositeDisposable()

    init {
        val database = HabitDatabase.getDatabase(application)
        habitDao = database.habitDao()
        itemsLD = habitDao.getAll()
        state = MutableLiveData()
    }

    fun startStop(){
        isStarted.value = !(isStarted.value!!)
    }

    fun stop(){
        isStarted.value = false
    }

    fun getById(id: Long?): Single<Habit> {
        return habitDao.getById(id)
    }

    override fun add(t: Habit) {
        state.value = State.loading()

        disposable.add(habitDao.insert(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.added("Habit added")},
                {state.value = State.error("Error")}))
    }

    override fun delete(t: Habit) {
        state.value = State.loading()

        disposable.add(habitDao.delete(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.removed("Habit removed")},
                {state.value = State.error("Error")}))
    }

    override fun deleteAll() {
        disposable.add(habitDao.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state.value = State.removed("All habits removed") },
                { state.value = State.error("Error") }))
    }

    override fun deleteSelected() {
        disposable.add(habitDao.deleteSelected(checkedItemsLD.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.removed("Habits removed")},
                {state.value = State.error("Error")}))
    }

    override fun update(t: Habit) {
        disposable.add(habitDao.update(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state.value = State.updated("Habit updated") },
                { state.value = State.error("Error") }))
    }


}