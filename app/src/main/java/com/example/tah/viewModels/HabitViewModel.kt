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
) : BaseViewModel<Habit>()
{
    val editable = MutableLiveData(false)

    init {
        itemsLD = repository.getAll()
    }

    fun setEditable(isEditable: Boolean) {
        editable.value = isEditable
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

    override fun update(t: Habit) {
        state.value = State.loading()
        viewModelScope.launch {
            repository.update(t)
            state.value = State.updated("Habit updated")
        }
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun deleteSelected() {
        TODO("Not yet implemented")
    }
}
