package com.example.tah.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tah.dao.habit.HabitRepository
import com.example.tah.models.Habit
import com.example.tah.utilities.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitStartedViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel()
{
    val habit = MutableLiveData<Habit>()
    val state = MutableLiveData<State>()
    val isStarted = MutableLiveData(false)

    fun startStop() {
        isStarted.value = !(isStarted.value!!)
    }

    fun getById(habitId: Long) {
        viewModelScope.launch {
            habit.value = repository.getById(habitId)
        }
    }

    fun updateSessionLength(sessionLength: Long) {
        viewModelScope.launch {
            habit.value?.sessionLength = sessionLength
            repository.updateSessionLength(habit.value?.sessionLength!!, habit.value?.id!!)
        }
    }


}