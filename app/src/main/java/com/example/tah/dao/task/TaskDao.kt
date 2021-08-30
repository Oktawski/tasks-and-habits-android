package com.example.tah.dao.task

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.tah.dao.BaseDao
import com.example.tah.models.Task
import com.example.tah.models.TaskWithTodos


@Dao
interface TaskDao : BaseDao<Task> {

    @Query("SELECT * FROM tasks ORDER BY taskId ASC")
    fun getAll(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE taskId=:taskId LIMIT 1")
    suspend fun getTaskById(taskId: Int?): Task

   /* @Query("SELECT * FROM tasks WHERE type = :taskType")
    suspend fun getFilteredTasks(taskType: TaskType)*/

    @Query("DELETE FROM tasks WHERE taskId in (:idList)")
    suspend fun deleteSelected(idList: List<Int>): Int

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    suspend fun getTaskWithTodosByTaskId(taskId: Int?): TaskWithTodos

}