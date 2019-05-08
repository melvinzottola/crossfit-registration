package com.crossfit.registration.action

import com.crossfit.registration.application.DateManager
import com.crossfit.registration.model.Registration
import com.crossfit.registration.model.Section
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.time.LocalDate

/**
 * This test is an integration test
 * The values concerning the registrationDate and the hours must
 * be set on purpose.
 */
@RunWith(JUnitPlatform::class)
class RegistrationActionSpec : Spek({
    given("a user connected") {
        val credentials = TestUtils.getSetup().credentials
        val httpClient = TestUtils.getHttpConnection()
        AuthenticationAction(credentials).request(httpClient)
        on("registration action") {
            val registrationDate = "2019-05-03"
            val dateManager = DateManager(LocalDate.of(2019, 5, 3))
            val registration = Registration(Section.WOD_ATHLET_SMALL_ZONE, "1530")
            val registrationBlock = GetPlanningAction(registration, dateManager)
                    .request(httpClient)
            val successful = RegistrationAction(registration, registrationDate, registrationBlock)
                    .request(httpClient)
            it("should be registered") {
                Assertions.assertThat(successful).isTrue()
            }
        }
    }
})