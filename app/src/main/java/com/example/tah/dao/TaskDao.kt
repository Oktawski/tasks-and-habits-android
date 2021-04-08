package com.example.tah.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.tah.models.Task
import io.reactivex.Completable

@Dao
interface TaskDao: BaseDao<Task> {

    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getAll(): LiveData<List<Task>>

    @Query("DELETE FROM tasks WHERE id in (:idList)")
    fun deleteSelected(idList: List<Int>)

    @Query("DELETE FROM tasks")
    fun deleteAll()
}