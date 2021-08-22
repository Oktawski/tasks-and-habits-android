package com.example.tah.utilities

import androidx.room.TypeConverter
import com.example.tah.models.TaskType

class Converters {

    companion object {
        @TypeConverter
        fun toType(value: String) = enumValueOf<TaskType>(value)

        @TypeConverter
        fun fromType(value: TaskType) = value.name
    }
}