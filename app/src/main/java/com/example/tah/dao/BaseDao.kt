package com.example.tah.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import io.reactivex.Completable


interface BaseDao<T> {

    @Insert
    suspend fun insert(t: T): Long

    @Delete
    fun delete(t: T): Completable

    @Update
    fun update(t: T): Completable
}