package com.example.tah.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {

    @Insert
    fun insert(t: T)

    @Update
    fun update(t: T)

    @Delete
    fun delete(t: T)
}