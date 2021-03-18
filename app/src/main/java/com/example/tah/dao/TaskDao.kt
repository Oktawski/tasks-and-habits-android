package com.example.tah.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.tah.models.Task

@Dao
interface TaskDao: BaseDao<Task>{

    @Query("SELECT * FROM tasks ORDER BY id ASC")
    fun getAll(): LiveData<List<Task>>

    @Query("DELETE FROM tasks")
    fun deleteAll()
}