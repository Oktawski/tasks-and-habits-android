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
    internal val checkedItemsLD = MutableLiveData<List<Int>>(mutableListOf())
    private val disposable = CompositeDisposable()

    suspend fun add(t: Task): Long {
        state.value = State.loading()
        trimLeadingAndTrailingWhitespaces(t)

        val taskId = taskDao.insert(t)
        state.value = State.added("Task added")
        return taskId
    }

    fun getAll(): LiveData<List<Task>> {
        return taskDao.getAll()
    }

    fun getFiltered(type: TaskType): LiveData<List<Task>> {
        return  taskDao.getFilteredTasks(type)
    }

    suspend fun getTaskById(id: Int): Task {
        return taskDao.getTaskById(id)
    }

    suspend fun delete(t: Task) {
        if (taskDao.delete(t) == 1) state.value = State.removed("Task removed")
        else state.value = State.error("Error")
    }

    suspend fun deleteAll() {
        taskDao.deleteAll()
    }

    suspend fun deleteSelected() {
        val deletedCount = taskDao.deleteSelected(checkedItemsLD.value!!)
        if(deletedCount > 1) state.value = State.removed("Tasks removed")
        else state.value = State.removed("Task removed")
    }

    fun update(t: Task) {
        trimLeadingAndTrailingWhitespaces(t)

        disposable.add(taskDao.update(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({state.value = State.updated("Task updated")},
                {state.value = State.error("Task not updated")}))
    }

    suspend fun getTaskWithTodosByTaskId(id: Int) = taskDao.getTaskWithTodosByTaskId(id)

    suspend fun addTaskWithTodos(taskWithTodos: TaskWithTodos) {
        val taskId = taskDao.insert(taskWithTodos.task!!)
        for (todo in taskWithTodos.todos!!) {
            todo.taskId = taskId.toInt()
            todoDao.insert(todo)
        }
    }
}