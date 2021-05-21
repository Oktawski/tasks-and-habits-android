package com.example.tah.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todo(
        @PrimaryKey(autoGenerate = true) val id: Int?,
        @ColumnInfo(name = "name") @NonNull var name: String,
        @ColumnInfo(name = "is_complete") var isComplete: Boolean = false
)