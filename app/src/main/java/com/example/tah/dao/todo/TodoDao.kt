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
    fun getAllByTaskId(id: Int): LiveData<List<Todo>>

    @Query("SELECT * FROM todos WHERE taskId = :id ORDER BY todoId ASC")
    suspend fun getTodosByTaskId(id: Int): List<Todo>

    @Query("SELECT * FROM todos WHERE todoId = :id LIMIT 1")
    fun getById(id: Int): Single<Todo>

    @Query("SELECT * FROM todos WHERE is_complete = 1 AND taskId = :taskId")
    fun getCompletedByTaskId(taskId: Int): LiveData<List<Todo>>

    @Query("DELETE FROM todos WHERE is_complete = 1 AND taskId = :taskId")
    fun deleteCompletedByTaskId(taskId: Int): Completable

    @Query("DELETE FROM todos")
    fun deleteAll(): Completable

    @Query("DELETE FROM todos WHERE todoId IN (:idList)")
    suspend fun deleteMultiple(idList: List<Int>)
}