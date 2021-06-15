package com.example.tah.utilities

interface ViewHabitTime {

    fun getTime(secondsData : Long): Map<String,Long>
    {
        var minutes : Long = secondsData % 3600
        val hours : Long = (secondsData - minutes)/3600

        val seconds : Long = minutes % 60
        minutes  = (minutes - seconds)/60

        return mapOf("Hours" to hours, "Minutes" to minutes, "Seconds" to seconds)
    }

}