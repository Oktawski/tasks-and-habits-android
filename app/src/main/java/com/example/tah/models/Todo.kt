package com.example.tah.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
class Todo(
        @PrimaryKey(autoGenerate = true) val id: Int?,
        @ColumnInfo(name = "name") @NonNull var name: String,
        @ColumnInfo(name = "is_complete") var isComplete: Boolean = false
) {
    companion object: ViewType {
        override fun getDetailsView(): Int {
            TODO("Not yet implemented")
        }

        override fun getAddView(): Int {
            TODO("Not yet implemented")
        }

        override fun getItemView(): Int {
            TODO("Not yet implemented")
        }

    }

}