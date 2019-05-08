package com.crossfit.registration.file

import com.crossfit.registration.model.Section
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.nio.file.Files
import java.nio.file.Paths
import java.time.DayOfWeek

@RunWith(JUnitPlatform::class)
class SetupReaderSpec : Spek({
    given("classic setup reader") {
        val setupReader = SetupReader()
        on("find setups file") {
            val setupPath = Files.createFile(Paths.get(System.getProperty("user.dir"), "setup-user1.txt"))
            val listOfSetupFiles = setupReader.findAllSetupPaths()
            Files.delete(setupPath)
            it("should contain one setup file") {
                Assert.assertEquals(1, listOfSetupFiles.size)
            }
        }
        on("read setup file") {
            val setupPath = Files.createTempFile("setup-temp", ".txt")
            val content = listOf("user;password", "MONDAY;WOD_ATHLET_TALL_ZONE;1830", "TUESDAY;WOD_ATHLET_TALL_ZONE;1830")
            Files.write(setupPath, content)
            val setup = setupReader.read(DayOfWeek.MONDAY, setupPath)
            Files.delete(setupPath)
            it("should contain setup values") {
                Assert.assertEquals(setup.credentials.name, "user")
                Assert.assertEquals(setup.credentials.password, "password")
                Assert.assertEquals(setup.registrations.size, 1)
                Assert.assertEquals(setup.registrations.get(0).section, Section.WOD_ATHLET_TALL_ZONE)
                Assert.assertEquals(setup.registrations.get(0).hours, "1830")
            }
        }
    }
})