package com.example.tah.viewModels

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import com.example.tah.dao.habit.HabitRepository
import com.example.tah.models.Habit
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HabitViewModel(@NonNull application: Application):
    BaseViewModel<Habit>(application)
{
    private val repository: HabitRepository = HabitRepository(application)

    val habitTime = MutableLiveData<Long>()
    val isStarted = MutableLiveData(false)

    init {
        itemsLD = repository.getHabitsLD()
        state = repository.getState()
    }

    fun startStop(){
        isStarted.value = !(isStarted.value!!)
    }

    fun stop(){
        isStarted.value = false
    }

    fun getHabitLD(id: Long){
        repository.getById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({habitTime.value = it.sessionLength},{})
    }

    fun getById(id: Long?): Single<Habit> {
        return repository.getById(id)
    }

    override fun add(t: Habit) {
        repository.add(t)
    }

    override fun delete(t: Habit) {
        repository.delete(t)
    }

    override fun deleteAll() {
        repository.deleteAll()
    }

    override fun deleteSelected() {
        TODO("Not yet implemented")
    }

    override fun update(t: Habit) {
        repository.update(t)
    }


}