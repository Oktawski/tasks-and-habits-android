package com.example.tah.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.tah.models.Task

@Dao
interface TaskDao: BaseDao<Task> {

    @Query("SELECT * FROM tasks ORDER BY id")
    fun getAll(): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getById(taskId: Int): Task
}