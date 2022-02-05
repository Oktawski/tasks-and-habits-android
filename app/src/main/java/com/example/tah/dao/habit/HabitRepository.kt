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

    suspend fun getById(id: Long?): Habit = dao.getById(id)

    suspend fun add(t: Habit): Long {
        trimLeadingAndTrailingWhitespaces(t)
        return dao.insert(t)
    }

    suspend fun delete(t: Habit) = dao.delete(t)

    suspend fun update(t: Habit) {
        trimLeadingAndTrailingWhitespaces(t)
        dao.updateS(t)
    }

    suspend fun update(t: Habit, id: Long) {
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val habit = getById(id)
            habit.name = t.name
            habit.description = t.description
            habit.sessionLength = t.sessionLength
            trimLeadingAndTrailingWhitespaces(habit)
            dao.updateS(habit)
        }
    }

    suspend fun updateSessionLength(sessionLength: Long, id: Long) {
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val habit = getById(id)
            habit.sessionLength = sessionLength
            dao.updateS(habit)
        }
    }
}
