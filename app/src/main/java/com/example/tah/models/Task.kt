package com.example.tah.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.tah.R

@Entity(tableName = "tasks")
data class Task(
        @PrimaryKey(autoGenerate = true) val id: Int?,

        @ColumnInfo(name = "name")
        @NonNull
        var name: String,

        @ColumnInfo(name = "description") var description: String?,

        @ColumnInfo(name = "is_complete") val isComplete: Boolean = false
){
        @Ignore
        constructor(name: String, description: String?, isComplete: Boolean)
        :this(null, name, description, isComplete)

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
