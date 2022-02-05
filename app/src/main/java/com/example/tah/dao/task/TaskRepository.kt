package com.example.tah.dao.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tah.dao.todo.TodoDao
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import com.example.tah.models.TaskWithTodos
import com.example.tah.utilities.PropertiesTrimmer
import com.example.tah.utilities.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val todoDao: TodoDao
) : PropertiesTrimmer {

    val state: MutableLiveData<State> = MutableLiveData()
    internal val checkedItemsLD = MutableLiveData<List<Long>>(mutableListOf())

    suspend fun add(t: Task): Long {
        trimLeadingAndTrailingWhitespaces(t)
        return taskDao.insert(t)
    }

    fun getAll(): LiveData<List<Task>> {
        return taskDao.getAll()
    }

    fun getFiltered(type: TaskType): LiveData<List<Task>> {
        return  taskDao.getFilteredTasks(type)
    }

    suspend fun getTaskById(id: Long): Task {
        return taskDao.getTaskById(id)
    }

    suspend fun delete(t: Task): Int {
        return taskDao.delete(t)
    }

    suspend fun deleteAll() {
        taskDao.deleteAll()
    }

    // TODO move liveData to viewModel and remove state changes
    suspend fun deleteSelected() {
        val deletedCount = taskDao.deleteSelected(checkedItemsLD.value!!)
        if(deletedCount > 1) state.value = State.removed("Tasks removed")
        else state.value = State.removed("Task removed")
    }

    suspend fun update(t: Task) {
        trimLeadingAndTrailingWhitespaces(t)
        taskDao.updateS(t)
    }

    suspend fun addTaskWithTodos(taskWithTodos: TaskWithTodos): Long {
        val taskId = taskDao.insert(taskWithTodos.task)
        for (todo in taskWithTodos.todos!!) {
            todo.taskId = taskId
            todoDao.insert(todo)
        }
        return taskId
    }
}
