package com.example.tah.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.tah.models.Todo

@Dao
interface TodoDao: BaseDao<Todo> {

    @Query("SELECT * FROM todos ORDER BY id ASC")
    fun getAll(): LiveData<List<Todo>>
}