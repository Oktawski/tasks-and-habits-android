package com.example.tah.dao.todo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.tah.dao.BaseDao
import com.example.tah.models.Todo
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface TodoDao : BaseDao<Todo> {

    @Query("SELECT * FROM todos WHERE taskId = :id ORDER BY todoId ASC")
    fun getAllByTaskId(id: Long): LiveData<List<Todo>>

    @Query("SELECT * FROM todos WHERE todoId = :id LIMIT 1")
    fun getById(id: Long): Single<Todo>

    @Query("SELECT * FROM todos WHERE is_complete = 1 AND taskId = :taskId")
    fun getCompletedByTaskId(taskId: Long): LiveData<List<Todo>>

    @Query("DELETE FROM todos WHERE is_complete = 1 AND taskId = :taskId")
    suspend fun deleteCompletedByTaskId(taskId: Long)

    @Query("DELETE FROM todos WHERE taskId = :taskId")
    suspend fun deleteTodosByTaskId(taskId: Long)

    @Query("DELETE FROM todos WHERE todoId IN (:idList)")
    suspend fun deleteMultiple(idList: List<Long>)
}