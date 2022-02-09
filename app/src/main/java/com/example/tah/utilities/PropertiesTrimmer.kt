package com.example.tah.utilities

import com.example.tah.models.Habit
import com.example.tah.models.Task
import com.example.tah.models.Todo

interface PropertiesTrimmer {

    fun trimLeadingAndTrailingWhitespaces(item: Any) {
        when (item) {
            is Task -> item.apply {
                            name = name.trim()
                            description = description?.trim()
            }
            is Todo -> item.apply {
                            name = name.trim()
            }
            is Habit -> item.apply {
                            name.trim()
                            description = description?.trim()
            }
        }
    }
}
