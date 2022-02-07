package com.example.tah.dao.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.tah.dao.BaseDao
import com.example.tah.models.Task
import com.example.tah.models.TaskType
import com.example.tah.models.TaskWithTodos


@Dao
interface TaskDao : BaseDao<Task> {

    @Query("SELECT * FROM tasks ORDER BY taskId ASC")
    fun getAll(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE taskId=:taskId LIMIT 1")
    suspend fun getTaskById(taskId: Long?): Task

    @Query("SELECT * FROM tasks WHERE type = :taskType ORDER BY taskId ASC")
    fun getFilteredTasks(taskType: TaskType): LiveData<List<Task>>

    @Query("DELETE FROM tasks WHERE taskId in (:selectedTasksIds)")
    suspend fun deleteSelected(selectedTasksIds: List<Long>): Int

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    suspend fun getTaskWithTodosByTaskId(taskId: Long?): TaskWithTodos

}