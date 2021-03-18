package com.example.tah.dao;

import androidx.room.Delete;
import androidx.room.Insert;

public interface BaseDao<T> {

    @Insert
    void insert(T t);

    @Delete
    void delete(T t);
}
