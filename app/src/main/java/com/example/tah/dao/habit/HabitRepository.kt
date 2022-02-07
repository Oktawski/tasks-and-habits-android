package com.example.tah.dao.habit

import androidx.lifecycle.LiveData
import com.example.tah.models.Habit
import com.example.tah.utilities.PropertiesTrimmer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HabitRepository @Inject constructor(
    private val dao: HabitDao
) : PropertiesTrimmer
{
    fun getAll(): LiveData<List<Habit>> = dao.getAll()

    suspend fun getById(habitId: Long?): Habit = dao.getById(habitId)

    suspend fun add(habit: Habit): Long {
        trimLeadingAndTrailingWhitespaces(habit)
        return dao.insert(habit)
    }

    suspend fun update(habit: Habit) {
        trimLeadingAndTrailingWhitespaces(habit)
        dao.updateS(habit)
    }

    suspend fun updateSessionLength(sessionLength: Long, habitId: Long) {
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val habitToUpdate = getById(habitId)
            habitToUpdate.sessionLength = sessionLength
            dao.updateS(habitToUpdate)
        }
    }

    suspend fun delete(habit: Habit) = dao.delete(habit)
}
