package com.farzadtara.zoroastrian.shared

import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/** NOAA-style apparent sunrise estimate. Returns Unix seconds, or NaN when sunrise does not occur. */
class SunriseCalculator {
    fun sunriseEpochSeconds(
        year: Int,
        month: Int,
        day: Int,
        latitude: Double,
        longitude: Double,
    ): Double {
        if (year !in 1..9999 || month !in 1..12 || latitude !in -90.0..90.0 || longitude !in -180.0..180.0) {
            return Double.NaN
        }

        val days = daysInMonth(year, month)
        if (day !in 1..days) return Double.NaN

        val epochDay = daysFromCivil(year, month, day)
        val dayOfYear = epochDay - daysFromCivil(year, 1, 1) + 1
        val yearLength = if (isLeapYear(year)) 366.0 else 365.0
        val gamma = 2.0 * PI / yearLength * (dayOfYear - 1)

        val equationOfTime = 229.18 * (
            0.000075 + 0.001868 * cos(gamma) - 0.032077 * sin(gamma) -
                0.014615 * cos(2.0 * gamma) - 0.040849 * sin(2.0 * gamma)
            )
        val declination = 0.006918 - 0.399912 * cos(gamma) + 0.070257 * sin(gamma) -
            0.006758 * cos(2.0 * gamma) + 0.000907 * sin(2.0 * gamma) -
            0.002697 * cos(3.0 * gamma) + 0.00148 * sin(3.0 * gamma)

        val latitudeRadians = latitude * PI / 180.0
        val cosineHourAngle = (
            cos(90.833 * PI / 180.0) / (cos(latitudeRadians) * cos(declination)) -
                tan(latitudeRadians) * tan(declination)
            )
        if (cosineHourAngle !in -1.0..1.0) return Double.NaN

        val hourAngleDegrees = acos(cosineHourAngle) * 180.0 / PI
        val utcMinutes = 720.0 - 4.0 * (longitude + hourAngleDegrees) - equationOfTime
        return epochDay * SECONDS_PER_DAY + utcMinutes * 60.0
    }

    private fun daysInMonth(year: Int, month: Int): Int = when (month) {
        2 -> if (isLeapYear(year)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }

    private fun isLeapYear(year: Int): Boolean = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)

    // Proleptic Gregorian civil date to Unix epoch day.
    private fun daysFromCivil(year: Int, month: Int, day: Int): Long {
        val adjustedYear = year - if (month <= 2) 1 else 0
        val era = adjustedYear / 400
        val yearOfEra = adjustedYear - era * 400
        val shiftedMonth = month + if (month > 2) -3 else 9
        val dayOfYear = (153 * shiftedMonth + 2) / 5 + day - 1
        val dayOfEra = yearOfEra * 365 + yearOfEra / 4 - yearOfEra / 100 + dayOfYear
        return (era * 146097 + dayOfEra - 719468).toLong()
    }

    private companion object {
        const val SECONDS_PER_DAY = 86400.0
    }
}
