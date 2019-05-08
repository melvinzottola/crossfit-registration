package com.crossfit.registration.action

import org.apache.http.impl.client.CloseableHttpClient
import java.io.IOException

interface HttpAction<T> {
    @Throws(IOException::class)
    fun request(httpClient: CloseableHttpClient): T
}