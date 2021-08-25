package com.example.tah.dao.task

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.tah.dao.BaseDao
import com.example.tah.models.Task
import com.example.tah.models.TaskWithTodos
import com.example.tah.models.Todo
import io.reactivex.Single


@Dao
abstract class TaskDao : BaseDao<Task> {

    @Query("SELECT * FROM tasks ORDER BY taskId ASC")
    abstract fun getAll(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE taskId=:taskId")
    abstract fun getById(taskId: Int?): Single<Task>

    @Query("SELECT * FROM tasks WHERE taskId=:taskId LIMIT 1")
    abstract suspend fun getTaskById(taskId: Int?): Task

    @Query("DELETE FROM tasks WHERE taskId in (:idList)")
    abstract fun deleteSelected(idList: List<Int>): Single<Int>

    @Query("DELETE FROM tasks")
    abstract fun deleteAll(): Single<Int>

    @Transaction
    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    abstract suspend fun getTaskWithTodosByTaskId(taskId: Int?): TaskWithTodos

}