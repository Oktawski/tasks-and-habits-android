package com.example.tah.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "description") val description: String?,
        @ColumnInfo(name = "is_complete") val isComplete: Boolean
)
