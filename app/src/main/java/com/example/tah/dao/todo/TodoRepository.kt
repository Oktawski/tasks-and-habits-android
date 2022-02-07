package com.example.tah.dao.todo

import androidx.lifecycle.LiveData
import com.example.tah.models.Todo
import com.example.tah.utilities.PropertiesTrimmer
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoDao: TodoDao
) : PropertiesTrimmer
{
    fun getAllByTaskId(taskId: Long): LiveData<List<Todo>>
        = todoDao.getAllByTaskId(taskId)

    fun getCompletedByTaskId(taskId: Long): LiveData<List<Todo>>
        = todoDao.getCompletedByTaskId(taskId)

    suspend fun add(todo: Todo): Long {
        trimLeadingAndTrailingWhitespaces(todo)
        return todoDao.insert(todo)
    }

    suspend fun update(todo: Todo) {
        trimLeadingAndTrailingWhitespaces(todo)
        todoDao.updateS(todo)
    }

    suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
    }

    suspend fun deleteCompletedByTaskId(taskId: Long) {
        todoDao.deleteCompletedByTaskId(taskId)
    }

    suspend fun deleteTodosByTaskId(taskId: Long) {
        todoDao.deleteTodosByTaskId(taskId)
    }

    fun deleteAll() {}
}
