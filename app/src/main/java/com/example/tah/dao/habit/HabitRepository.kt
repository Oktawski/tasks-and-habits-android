package com.example.tah.dao.habit

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tah.models.Habit
import com.example.tah.utilities.State
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HabitRepository(application: Application) {

    private var habitDao: HabitDao
    private var habitsLD: LiveData<List<Habit>>
    private var state: MutableLiveData<State> = MutableLiveData()

    private val disposable = CompositeDisposable()

    init{
        val database: HabitDatabase = HabitDatabase.getDatabase(application)
         habitDao = database.habitDao()
         habitsLD = habitDao.getAll()
    }

    fun getState(): MutableLiveData<State> = state;

    fun getHabitsLD(): LiveData<List<Habit>>{
        return habitsLD
    }

    fun add(habit: Habit){
        state.value = State.loading();

        disposable.add(habitDao.insert(habit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({state.value = State.added("Habit added")},
                        {state.value = State.error("Error")}))
    }

    fun getById(id: Long?): Single<Habit> {
        return habitDao.getById(id)
    }

    fun delete(habit: Habit){
        state.value = State.loading()

        disposable.add(habitDao.delete(habit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({state.value = State.removed("Habit removed")},
                        {state.value = State.error("Error")}))
    }

    fun deleteSelected(idList: List<Int>){
        disposable.add(habitDao.deleteSelected(idList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({state.value = State.removed("Habits removed")},
                        {state.value = State.error("Error")}))
    }

    fun deleteAll() {
        disposable.add(habitDao.deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ state.value = State.removed("All habits removed") },
                        { state.value = State.error("Error") }))
    }

    fun  update(habit: Habit){
        disposable.add(habitDao.update(habit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ state.value = State.updated("Habit updated") },
                        { state.value = State.error("Error") }))
    }
}