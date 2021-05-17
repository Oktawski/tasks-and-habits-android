package com.example.tah.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tah.R

@Entity(tableName = "Habits")
class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long?,

    @ColumnInfo(name = "name")
    @NonNull
    var name: String,

    @ColumnInfo(name = "description") var description: String?,

    @ColumnInfo(name = "timesInWeek") var timesInWeek: Int? = 1,

    @ColumnInfo(name = "timesDone") var timesDone: Int? = 0,

    @ColumnInfo(name = "sessionLength") var sessionLength: Int?,

    @ColumnInfo(name = "isComplete") val isComplete: Boolean = false
) {
    constructor(name: String, description: String?, timesInWeek: Int?, timesDone: Int?, sessionLength: Int?, isComplete: Boolean)
    :this(null, name, description, timesInWeek, timesDone, sessionLength, isComplete )

    companion object: ViewType{
        override  fun getAddView(): Int{
            return R.layout.add_habit_fragment
        }

        override fun getDetailsView(): Int {
            return R.layout.fragment_habits
        }

        override fun getItemView(): Int {
            return R.layout.item_habit
        }
    }
}
