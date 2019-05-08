package com.crossfit.registration.action

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
class AuthenticationActionSpec : Spek({
    given("valid credentials") {
        val credentials = TestUtils.getSetup().credentials
        on("connecting to website") {
            val httpClient = TestUtils.getHttpConnection()
            val isAuthenticated = AuthenticationAction(credentials).request(httpClient)
            it("should be authenticated") {
                assertThat(isAuthenticated).isTrue()
            }
        }
    }
})