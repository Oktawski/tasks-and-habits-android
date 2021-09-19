package com.example.tah.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tah.dao.task.TaskRepository
import com.example.tah.dao.todo.TodoRepository
import com.example.tah.models.Task
import com.example.tah.models.Todo
import com.example.tah.utilities.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val todoRepository: TodoRepository,
) : ViewModel()
{
    var task = MutableLiveData<Task>()
    lateinit var todos: LiveData<List<Todo>>


    val state: MutableLiveData<State> = taskRepository.state

    fun init(taskId: Int) {
        viewModelScope.launch {
            task.value = taskRepository.getTaskById(taskId)
            todos = todoRepository.getAll(taskId)
        }
    }

    suspend fun getTaskById(taskId: Int): Task {
        return taskRepository.getTaskById(taskId)
    }

    suspend fun delete() {
        taskRepository.delete(task.value!!)
    }

    fun update() {
        taskRepository.update(task.value!!)
    }

    suspend fun delete(task: Task) {
        taskRepository.delete(task)
    }

    fun update(task: Task) {
        taskRepository.update(task)
    }

}
