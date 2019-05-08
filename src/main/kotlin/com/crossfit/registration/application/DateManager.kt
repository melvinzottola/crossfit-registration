package com.crossfit.registration.application

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

class DateManager(date: LocalDate) {
    val registrationDateFormatted: String
    val startPlanningFormatted: String
    val endPlanningFormatted: String

    init {
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        registrationDateFormatted = date.format(pattern)
        val diffDayOfWeek = (1 - date.dayOfWeek.value).absoluteValue
        val startDate = date.minusDays(diffDayOfWeek.toLong())
        startPlanningFormatted = startDate.format(pattern)
        endPlanningFormatted = startDate.plusDays(7).format(pattern)
    }
}