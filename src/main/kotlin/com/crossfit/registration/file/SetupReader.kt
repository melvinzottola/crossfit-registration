package com.crossfit.registration.file

import com.crossfit.registration.model.Credentials
import com.crossfit.registration.model.Registration
import com.crossfit.registration.model.Section
import com.crossfit.registration.model.Setup
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.DayOfWeek
import java.util.stream.Collectors

open class SetupReader {
    fun findAllSetupPaths(): List<Path> =
            Files.list(Paths.get(System.getProperty("user.dir")))
                    .filter { it.fileName.toString().endsWith(".txt") }
                    .collect(Collectors.toList())

    fun read(dayOfWeek: DayOfWeek, setupPath: Path): Setup {
        val lines = Files.newBufferedReader(setupPath)
                .lines()
                .filter { !it.trim().startsWith("#") }
                .collect(Collectors.toList())
        val credentials = getCredentials(lines.removeAt(0))
        val registrations = getRegistrations(dayOfWeek.toString(), lines)

        return Setup(credentials, registrations)
    }

    private fun getCredentials(line: String): Credentials {
        val split = line.split(";")
        return Credentials(split[0], split[1])
    }

    private fun getRegistrations(dayOfWeekToString: String, lines: List<String>): List<Registration> =
            lines.stream()
                    .filter { it.startsWith(dayOfWeekToString) }
                    .map { it.split(";") }
                    .map { Registration(Section.valueOf(it.get(1)), it.get(2)) }
                    .collect(Collectors.toList())
}