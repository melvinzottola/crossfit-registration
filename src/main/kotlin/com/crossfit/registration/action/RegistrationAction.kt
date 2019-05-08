package com.crossfit.registration.action

import com.crossfit.registration.model.ActionURL
import com.crossfit.registration.model.Registration
import com.crossfit.registration.model.RegistrationBlock
import org.apache.http.NameValuePair
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.utils.URLEncodedUtils
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils

class RegistrationAction(private val registration: Registration,
                         private val registrationDate: String,
                         private val registrationBlock: RegistrationBlock) : HttpAction<Boolean> {
    override fun request(httpClient: CloseableHttpClient): Boolean {
        return when {
            registrationBlock.waitingQueue -> register(httpClient, true)
            else -> register(httpClient)
        }
    }

    private fun register(httpClient: CloseableHttpClient, waitingQueue: Boolean = false): Boolean {
        var successfull = false
        val paramsStr = URLEncodedUtils.format(buildParamsForRegistration(waitingQueue), "utf-8")
        val httpGet = HttpGet(ActionURL.REGISTRATION_URL.value + paramsStr)

        httpClient.execute(httpGet).use {
            val toString = EntityUtils.toString(it.entity)
            successfull = toString.contains(if (waitingQueue) "attente est valid" else "servation est valid")
        }
        return successfull
    }

    private fun buildParamsForRegistration(waitingQueue: Boolean): List<NameValuePair> =
            listOf(
                    BasicNameValuePair("action", if (waitingQueue) "sendresa_attente" else "sendresa"),
                    BasicNameValuePair("idcompte", "reebok"),
                    BasicNameValuePair("activite", registration.section.id),
                    BasicNameValuePair("refCRENO", registrationBlock.refId.toString()),
                    BasicNameValuePair("numCRENO", "1"),
                    BasicNameValuePair("lejour", registrationDate),
                    BasicNameValuePair("lecreno", registration.hours),
                    BasicNameValuePair("leidU", "0"),
                    BasicNameValuePair("resadirect", if (waitingQueue) "50" else "0"),
                    BasicNameValuePair("lenomU", "%20"),
                    BasicNameValuePair("leprenomU", "%20"),
                    BasicNameValuePair("letelU", "%20"),
                    BasicNameValuePair("lemailU", "%20"),
                    BasicNameValuePair("lemultiU", if (waitingQueue) "1" else ""),
                    BasicNameValuePair("effectif", "12")
            )
}