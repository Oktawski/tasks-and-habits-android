package com.example.tah.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
class Task(
        @PrimaryKey(autoGenerate = true) val id: Int?,

        @ColumnInfo(name = "name")
        @NonNull
        val name: String,

        @ColumnInfo(name = "description") val description: String?,

        @ColumnInfo(name = "is_complete") val isComplete: Boolean = false
){
        constructor(name: String, description: String?, isComplete: Boolean)
        :this(null, name, description, isComplete)
}
