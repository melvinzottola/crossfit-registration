package com.crossfit.registration.action

import com.crossfit.registration.file.SetupReader
import com.crossfit.registration.model.Setup
import org.apache.http.HttpHost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.DefaultProxyRoutePlanner
import java.nio.file.Paths
import java.time.DayOfWeek

class TestUtils {
    companion object {
        fun getHttpConnection(): CloseableHttpClient =
                HttpClients.createDefault()

        fun getSetup(): Setup {
            val osType = System.getProperty("os.name")
            val prefixPath = if (osType.toLowerCase().indexOf("mac") >= 0) "/" else ""
            val pathResourceSetup = Paths.get(prefixPath, TestUtils::javaClass.javaClass.classLoader.getResource("setup-test.txt").path.substring(1))
            return SetupReader().read(DayOfWeek.MONDAY, pathResourceSetup)
        }
    }
}