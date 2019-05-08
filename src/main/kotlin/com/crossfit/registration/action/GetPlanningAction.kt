package com.crossfit.registration.action

import com.crossfit.registration.application.DateManager
import com.crossfit.registration.model.ActionURL
import com.crossfit.registration.model.Registration
import com.crossfit.registration.model.RegistrationBlock
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils
import org.json.JSONArray
import org.json.JSONObject


class GetPlanningAction(private val registration: Registration,
                        private val dateManager: DateManager) : HttpAction<RegistrationBlock> {
    override fun request(httpClient: CloseableHttpClient): RegistrationBlock {
        httpClient.execute(
                HttpPost(ActionURL.FEED_PLANNING.value).apply {
                    entity = UrlEncodedFormEntity(
                            listOf(BasicNameValuePair("c", "reebok"),
                                    BasicNameValuePair("prati", "2428"),
                                    BasicNameValuePair("presta", registration.section.id),
                                    BasicNameValuePair("ep", "0"),
                                    BasicNameValuePair("mode", "0"),
                                    BasicNameValuePair("start", dateManager.startPlanningFormatted),
                                    BasicNameValuePair("end", dateManager.endPlanningFormatted))
                    )
                }
        ).use {
            val json = JSONObject("{root:${EntityUtils.toString(it.entity)}}")
            val hours = registration.hours
            val hoursFormatted = "${hours.substring(0, 2)}:${hours.substring(2)}:00"
            val dateToSearch = "${dateManager.registrationDateFormatted} $hoursFormatted"

            val root = json.get("root") as JSONArray
            for (i in 0 until root.length()) {
                val block = root.get(i) as JSONObject
                if (block.get("start") == dateToSearch) {
                    val refId = block.getInt("ref")
                    val ladispo = block.getString("ladispo")
                    // ladispo is of format "0 -> liste d'attente : 4" when in waiting queue
                    return RegistrationBlock(refId, ladispo.contains("attente"))
                }
            }
            throw RuntimeException("No registration block found for $hoursFormatted")
        }
    }
}