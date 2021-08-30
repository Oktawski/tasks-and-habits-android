package com.example.tah.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tah.models.TaskWithTodos

class TaskWithTodosViewModel : ViewModel() {

    val taskWithTodos = MutableLiveData<TaskWithTodos>()

}