package com.crossfit.registration.application

import com.crossfit.registration.action.AuthenticationAction
import com.crossfit.registration.action.GetPlanningAction
import com.crossfit.registration.action.RegistrationAction
import com.crossfit.registration.file.SetupReader
import org.apache.http.impl.client.HttpClients
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate


@Component
class DailyRegistrationScript {
    private val logger = LoggerFactory.getLogger(DailyRegistrationScript::class.java)

    @Scheduled(cron = "0 1 0 * * *")
    fun execute() {
        val setupReader = SetupReader()
        val setupPaths = setupReader.findAllSetupPaths()

        if (setupPaths.isEmpty()) {
            logger.warn("no setup path found")
            return
        }

        // compute dates
        val registrationDate = LocalDate.now().plusDays(4)
        val dateManager = DateManager(registrationDate)
        val registrationDateFormatted = dateManager.registrationDateFormatted

        logger.info("The date to register will be ${registrationDateFormatted}")

        setupPaths.forEach { setupPath ->
            val httpClient = HttpClients.createDefault()
            val setup = setupReader.read(registrationDate.dayOfWeek, setupPath)
            if (setup.registrations.isEmpty()) {
                logger.info("No registration for this day for user ${setup.credentials.name}")
            } else {
                logger.info("Trying to register for user ${setup.credentials.name}")
                if (AuthenticationAction(setup.credentials).request(httpClient)) {
                    setup.registrations.forEach { registration ->
                        val refRegistration = GetPlanningAction(registration, dateManager)
                                .request(httpClient)
                        logger.info("Registering for ${registration.section} at ${registration.hours}")
                        val success = RegistrationAction(registration, registrationDateFormatted, refRegistration)
                                .request(httpClient)
                        logger.info("Registration success: ${success}")
                    }
                } else {
                    logger.warn("Authentication failed")
                }
            }
            httpClient.close()
            // avoid too fast requests which could lead to java.net.SocketException: Connection reset
            Thread.sleep(1000)
        }
    }
}