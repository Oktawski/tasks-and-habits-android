package com.example.tah.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import io.reactivex.Completable;


public interface BaseDao<T> {

    @Insert
    Completable insert(T t);

    @Delete
    Completable delete(T t);

    @Update
    Completable update(T t);
}
