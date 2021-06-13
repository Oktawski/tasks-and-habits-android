package com.example.tah.viewModels

import android.app.Application
import android.util.Log
import androidx.annotation.NonNull
import com.example.tah.dao.habit.HabitRepository
import com.example.tah.models.Habit

class HabitViewModel(@NonNull application: Application):
    BaseViewModel<Habit>(application)
{
    private val repository: HabitRepository = HabitRepository(application)

    init {
        itemsLD = repository.getHabitsLD()
        state = repository.getState()
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