package com.example.tah.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tah.dao.habit.HabitRepository
import com.example.tah.models.Habit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repository: HabitRepository
) : BaseViewModel<Habit>() {

    val editable = MutableLiveData(false)
    val isStarted = MutableLiveData(false)

    init {
        itemsLD = repository.getAll()
        state = repository.state
    }

    fun startStop(){
        isStarted.value = !(isStarted.value!!)
    }

    fun getAll() = repository.getAll()

    suspend fun getByIdSus(id: Long?): Habit {
        return repository.getById(id)
    }

    override suspend fun add(t: Habit): Long {
        return repository.add(t)
    }

    override fun delete(t: Habit) {
        viewModelScope.launch {
            repository.delete(t)
        }
    }

    override fun deleteAll() {
        repository.deleteAll()
    }

    override fun deleteSelected() {
        repository.deleteSelected()
    }

    override fun update(t: Habit) {
        repository.update(t)
    }

}