package com.example.tah.models

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithTodos(
    @Embedded var task: Task,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "taskId"
    )
    val todos: MutableList<Todo>?
)