package com.example.tah.di

import android.content.Context
import androidx.room.Room
import com.example.tah.dao.habit.HabitDao
import com.example.tah.dao.habit.HabitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HabitDatabaseModule {

    @Provides
    @Singleton
    fun provideHabitDatabase(@ApplicationContext appContext: Context): HabitDatabase {
        return Room.databaseBuilder(
            appContext,
            HabitDatabase::class.java,
            "habit_database"
        ).build()
    }

    @Provides
    fun provideHabitDao(habitDatabase: HabitDatabase): HabitDao = habitDatabase.habitDao()
}