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

    fun init(taskId: Long) {
        viewModelScope.launch {
            task.value = taskRepository.getTaskById(taskId)
            todos = todoRepository.getAllByTaskId(taskId)
        }
    }

    suspend fun delete() {
        taskRepository.delete(task.value!!)
    }

    fun update(name: String, description: String) {
        state.value = State.loading()
        task.value?.name = name
        task.value?.description = description
        viewModelScope.launch {
            taskRepository.update(task.value!!)
            state.value = State.updated("Task updated")
        }
    }

    fun deleteTaskWithTodos()  {
        state.value = State.loading()
        viewModelScope.launch {
            todoRepository.deleteTodosByTaskId(task.value?.taskId!!)
            val removedCount = taskRepository.delete(task.value!!)
            state.value = if (removedCount == 1) State.removed("Task removed")
                            else State.error("Error lol")
        }
    }
}
