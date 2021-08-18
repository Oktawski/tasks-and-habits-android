package com.example.tah.utilities

import java.util.*


interface ViewHabitTime {

    fun getTime(secondsData: Long): Map<String, Long> {
        var minutes: Long = secondsData % 3600
        val hours: Long = (secondsData - minutes) / 3600

        val seconds: Long = minutes % 60
        minutes = (minutes - seconds) / 60

        // Cannot use below function, I don't know why, internet doesn't either
        //return mapOf("Hours" to hours, "Minutes" to minutes, "Seconds" to seconds)

        return HashMap<String, Long>(3).also { hashMap ->
            hashMap["Hours"] = hours
            hashMap["Minutes"] = minutes
            hashMap["Seconds"] = seconds
        }
    }

    fun getTimeStrings(secondsData: Long): Map<String, String>{
        val timeMap = getTime(secondsData)

        val hours = convertToString(timeMap["Hours"]!!)
        val minutes = convertToString(timeMap["Minutes"]!!)
        val seconds = convertToString(timeMap["Seconds"]!!)

        return HashMap<String, String>(3).also { hashMap ->
            hashMap["Hours"] = hours
            hashMap["Minutes"] = minutes
            hashMap["Seconds"] = seconds
        }
    }

    private fun convertToString(time: Long): String{
        return if(time < 10) "0$time" else time.toString()
    }
}