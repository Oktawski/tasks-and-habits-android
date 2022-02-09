package com.example.tah.utilities


interface TimeConverter {

    fun getTimeUnitsToValuesAsStrings(secondsToConvert: Long): Map<String, String>{
        var minutes: Long = secondsToConvert % 3600
        val hours: Long = (secondsToConvert - minutes) / 3600
        val seconds: Long = minutes % 60
        minutes = (minutes - seconds) / 60

        return mapOf(
            "Hours" to convertTimeUnitToString(hours),
            "Minutes" to convertTimeUnitToString(minutes),
            "Seconds" to convertTimeUnitToString(seconds)
        )
    }

    private fun convertTimeUnitToString(time: Long): String{
        return if(time < 10) "0$time" else time.toString()
    }
}