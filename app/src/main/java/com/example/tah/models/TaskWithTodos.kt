package com.example.tah.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class TaskWithTodos(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "taskId"
    )
    val todos: MutableList<Todo>?
)