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
/*

    @Query("SELECT * FROM todos ORDER BY todoId ASC")
    fun getAll(): LiveData<List<Todo>>
*/

    @Query("SELECT * FROM todos WHERE taskId = :id ORDER BY todoId ASC")
    fun getAllByTaskId(id: Int): LiveData<List<Todo>>

    @Query("SELECT * FROM todos WHERE todoId = :id LIMIT 1")
    fun getById(id: Int): Single<Todo>

    @Query("SELECT * FROM todos WHERE is_complete = 1")
    fun getCompletedList(): Single<List<Todo>>

    @Query("DELETE FROM todos WHERE is_complete = 1")
    fun deleteCompleted(): Completable
}