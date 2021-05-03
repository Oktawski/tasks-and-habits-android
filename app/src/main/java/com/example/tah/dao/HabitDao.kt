package com.example.tah.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.tah.models.Habit
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface HabitDao: BaseDao<Habit> {
    @Query("SELECT * FROM Habits ORDER BY id ASC")
    fun getAll(): LiveData<List<Habit>>

    @Query("SELECT * FROM Habits WHERE id=:habitId")
    fun getById(habitId: Int?): Single<Habit>

    @Query("DELETE FROM habits WHERE id in (:idList)")
    fun deleteSelected(idList: List<Int>): Single<Int>

    @Query( "DELETE FROM habits")
    fun deleteAll(): Single<Int>
}