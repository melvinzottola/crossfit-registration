package com.crossfit.registration.action

import com.crossfit.registration.model.ActionURL
import com.crossfit.registration.model.Credentials
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.message.BasicNameValuePair

class AuthenticationAction(private val credentials: Credentials) : HttpAction<Boolean> {
    override fun request(httpClient: CloseableHttpClient): Boolean {
        httpClient.execute(
                HttpPost(ActionURL.LOGIN_URL.value).apply {
                    entity = UrlEncodedFormEntity(
                            listOf(BasicNameValuePair("login", credentials.name),
                                    BasicNameValuePair("password", credentials.password),
                                    BasicNameValuePair("uniqid", "formulaire_connexion"),
                                    BasicNameValuePair("submit", "Connexion"))
                    )
                }
        ).use {
            return 302 == it.statusLine.statusCode
        }
    }
}