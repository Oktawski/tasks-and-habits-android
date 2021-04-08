package com.example.tah.dao;

import androidx.room.Delete;
import androidx.room.Insert;

import io.reactivex.Completable;


public interface BaseDao<T> {

    @Insert
    Completable insert(T t);

    @Delete
    void delete(T t);
}
