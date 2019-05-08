package com.crossfit.registration.application

import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.Month

@RunWith(JUnitPlatform::class)
class DateManagerSpec : Spek({
    given("a wednesday 2019/05/01") {
        val date = LocalDate.of(2019, Month.MAY, 1)
        on("new DateManager") {
            val dateManager = DateManager(date)
            it("should have weekDayStart on a monday 2019/04/29") {
                Assertions.assertThat(dateManager.startPlanningFormatted).isEqualTo("2019-04-29")
            }
        }
    }
    given("a monday 2019/04/29") {
        val date = LocalDate.of(2019, Month.APRIL, 29)
        on("new DateManager") {
            val dateManager = DateManager(date)
            it("should have weekDayStart on a monday 2019/04/29") {
                Assertions.assertThat(dateManager.startPlanningFormatted).isEqualTo("2019-04-29")
            }
        }
    }
})