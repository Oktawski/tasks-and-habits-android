package com.example.tah.utilities

import com.example.tah.models.Habit
import com.example.tah.models.Task
import com.example.tah.models.Todo

interface PropertiesTrimmer {
    fun trimLeadingAndTrailingWhitespaces(t: Task) {
        t.apply {
            name = name.trim()
            description = description?.trim()
        }
    }

    fun trimLeadingAndTrailingWhitespaces(t: Todo) {
        t.apply {
            name = name.trim()
        }
    }

    fun trimLeadingAndTrailingWhitespaces(t: Habit) {
        t.apply {
            name = name.trim()
            description = description?.trim()
        }
    }

}
