package com.example.tah.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import com.example.tah.models.TaskWithTodos
import com.example.tah.models.Todo

class TaskWithTodosViewModel : ViewModel() {

    val taskWithTodos = MutableLiveData(TaskWithTodos(Task(TaskType.SHOPPING), mutableListOf()))

    fun getTodos(): LiveData<List<Todo>> = MutableLiveData(taskWithTodos.value?.todos)

    fun addTodo(todo: Todo) {
        taskWithTodos.value?.todos?.add(todo)
    }

    fun setTask(task: Task) {
        taskWithTodos.value?.task = task
    }

}
