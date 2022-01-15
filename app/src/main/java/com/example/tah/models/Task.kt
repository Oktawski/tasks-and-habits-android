package com.example.tah.models

import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.tah.R

enum class TaskType {
        BASIC,
        HOME,
        SHOPPING,
        LEARNING
}

@Entity(tableName = "tasks")
data class Task(
        @PrimaryKey(autoGenerate = true)        val taskId: Long?,
        @ColumnInfo(name = "name") @NonNull     var name: String,
        @ColumnInfo(name = "description")       var description: String?,
        @ColumnInfo(name = "type")              var type: TaskType,
        @ColumnInfo(name = "is_complete")       val isComplete: Boolean = false,
) {
        @DrawableRes
        var imageResource: Int = -1

        init {
            imageResource = when (type) {
                    TaskType.SHOPPING -> R.drawable.ic_baseline_shopping_cart_24
                    TaskType.HOME -> R.drawable.ic_baseline_home_24
                    TaskType.BASIC -> R.drawable.ic_baseline_assignment_24
                    TaskType.LEARNING -> R.drawable.ic_baseline_school_24
            }
        }

        @Ignore
        constructor(name: String, description: String?, type: TaskType, isComplete: Boolean)
        :this(null, name, description, type, isComplete)

        @Ignore
        constructor(type: TaskType) : this(null, "", "", type, false)

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
