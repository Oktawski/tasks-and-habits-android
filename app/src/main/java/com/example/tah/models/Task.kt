package com.example.tah.models

import androidx.annotation.NonNull
import androidx.room.*
import com.example.tah.R

enum class TaskType {
        BASIC,
        HOME,
        SHOPPING,
        LEARNING
}

@Entity(tableName = "tasks")
data class Task(
        @PrimaryKey(autoGenerate = true)        val taskId: Int?,
        @ColumnInfo(name = "name") @NonNull     var name: String,
        @ColumnInfo(name = "description")       var description: String?,
        @ColumnInfo(name = "type")              var type: TaskType,
        @ColumnInfo(name = "is_complete")       val isComplete: Boolean = false,
) {
        @Ignore
        constructor(name: String, description: String?, type: TaskType, isComplete: Boolean)
        :this(null, name, description, type, isComplete)

        companion object: ViewType{
                override fun getAddView(): Int {
                        return R.layout.add_task_fragment
                }

                override fun getDetailsView(): Int {
                        return R.layout.fragment_tasks
                }

                override fun getItemView(): Int {
                        return R.layout.item_task
                }
        }
}
