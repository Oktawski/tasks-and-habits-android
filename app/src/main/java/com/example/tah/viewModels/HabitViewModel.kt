package com.example.tah.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tah.dao.habit.HabitRepository
import com.example.tah.models.Habit
import com.example.tah.utilities.State
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
        state = MutableLiveData<State>()
    }

    fun setEditable(isEditable: Boolean) {
        editable.value = isEditable
    }


    fun startStop(){
        isStarted.value = !(isStarted.value!!)
    }

    fun getAll() = repository.getAll()

    suspend fun getByIdSus(id: Long?): Habit {
        return repository.getById(id)
    }

    override fun add(t: Habit) {
        state.value = State.loading()
        viewModelScope.launch {
            repository.add(t)
            state.value = State.added("Habit added")
        }
    }

    override fun delete(t: Habit) {
        state.value = State.loading()
        viewModelScope.launch {
            repository.delete(t)
            state.value = State.removed("Habit removed")
        }
    }

    fun update(t: Habit, id: Long) {
        state.value = State.loading()
        viewModelScope.launch {
            repository.update(t, id)
            state.value = State.updated("Habit updated")
        }
    }

    fun updateSessionLength(sessionLength: Long, id: Long) {
        state.value = State.loading()
        viewModelScope.launch {
            repository.updateSessionLength(sessionLength, id)
            state.value = State.updated("Habit updated")
        }
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun deleteSelected() {
        TODO("Not yet implemented")
    }

    override fun update(t: Habit) {
        TODO("Not yet implemented")
    }
}
