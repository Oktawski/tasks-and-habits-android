package com.example.tah.dao.task

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.tah.dao.BaseDao
import com.example.tah.models.Task
import io.reactivex.Single


@Dao
interface TaskDao : BaseDao<Task> {

    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getAll(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE id=:taskId")
    fun getById(taskId: Int?): Single<Task>

    @Query("DELETE FROM tasks WHERE id in (:idList)")
    fun deleteSelected(idList: List<Int>): Single<Int>

    @Query("DELETE FROM tasks")
    fun deleteAll(): Single<Int>
}