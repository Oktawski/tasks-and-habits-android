package com.example.tah.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tah.R

@Entity(tableName = "Habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)    val id: Long?,
    @ColumnInfo(name = "name") @NonNull var name: String,
    @ColumnInfo(name = "description")   var description: String?,
    @ColumnInfo(name = "timesInWeek")   var timesInWeek: Int? = 1,
    @ColumnInfo(name = "timesDone")     var timesDone: Int? = 0,
    @ColumnInfo(name = "sessionLength") var sessionLength: Long,
    @ColumnInfo(name = "isComplete")    val isComplete: Boolean = false
) {

    companion object: ViewType{

        fun new(name: String, description: String?, hours: Int, minutes: Int): Habit{
            val habit = Habit(null, name, description, 0, 0, 0, false)
            habit.sessionLength = (hours * 60 * 60 + minutes * 60).toLong()
            return habit
        }

        override  fun getAddView(): Int{
            return R.layout.add_habit_fragment
        }

        override fun getDetailsView(): Int {
            return R.layout.details_habit
        }

        override fun getItemView(): Int {
            return R.layout.item_habit
        }
    }
}
