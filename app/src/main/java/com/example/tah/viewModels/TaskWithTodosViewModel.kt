package com.example.tah.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import com.example.tah.models.TaskWithTodos
import com.example.tah.models.Todo

class TaskWithTodosViewModel : ViewModel() {

    val taskWithTodos = MutableLiveData(TaskWithTodos(Task(TaskType.SHOPPING), mutableListOf()))
    val todos: MutableLiveData<MutableList<Todo>> = MutableLiveData(mutableListOf())

    fun getTodos(): LiveData<List<Todo>> = MutableLiveData(taskWithTodos.value?.todos)

    fun addTodo(todo: Todo) {
        //taskWithTodos.value?.todos?.add(todo)
        todos.value?.add(todo)
        Log.i("TASKWITHTODOSVM", "addTodo: ${todos.value?.size}")
    }

    fun setTask(task: Task) {
        taskWithTodos.value?.task = task
    }

}
