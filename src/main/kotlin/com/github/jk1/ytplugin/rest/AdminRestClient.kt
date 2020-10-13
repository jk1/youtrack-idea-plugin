package com.github.jk1.ytplugin.rest

import com.github.jk1.ytplugin.logger
import com.github.jk1.ytplugin.tasks.YouTrackServer
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.apache.commons.httpclient.NameValuePair
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.methods.PostMethod
import org.apache.commons.httpclient.methods.StringRequestEntity
import java.net.URL
import java.nio.charset.StandardCharsets

class AdminRestClient(override val repository: YouTrackServer) : AdminRestClientBase, RestClientTrait, ResponseLoggerTrait {

    override fun getVisibilityGroups(issueId: String): List<String> {
        val getGroupsUrl = "${repository.url}/api/visibilityGroups"
        val method = PostMethod(getGroupsUrl)
        method.params.contentCharset = "UTF-8"

        val top = NameValuePair("\$top", "-1")
        val fields = NameValuePair("fields", "groupsWithoutRecommended(name),recommendedGroups(name)")
        method.setQueryString(arrayOf(top, fields))

        val res: URL? = this::class.java.classLoader.getResource("admin_body.json")
        val jsonBody = res?.readText()?.replace("{issueId}", issueId, true)

        method.requestEntity = StringRequestEntity(jsonBody, "application/json", StandardCharsets.UTF_8.name())
        return method.connect {
            when (val status = httpClient.executeMethod(method)) {
                200 -> {
                    logger.debug("Successfully fetched visibility groups in AdminRestClient: code $status")
                    listOf("All Users") +
                            parseGroupNames(method, "recommendedGroups") +
                            parseGroupNames(method, "groupsWithoutRecommended")
                }
                else -> {
                    logger.debug("Failed to fetch visibility groups in AdminRestClient, code $status: ${method.responseBodyAsLoggedString()}")
                    throw RuntimeException(method.responseBodyAsLoggedString())
                }
            }
        }
    }

    private fun parseGroupNames(method: PostMethod, elem: String): List<String> {
        val myObject: JsonObject = JsonParser.parseReader(method.responseBodyAsReader) as JsonObject
        val recommendedGroups: JsonArray = myObject.get(elem) as JsonArray
        return recommendedGroups.map { it.asJsonObject.get("name").asString }
    }

    override fun getAccessibleProjects(): List<String> {
        val method = GetMethod("${repository.url}/api/admin/projects")
        val fields = NameValuePair("fields", "shortName")
        method.setQueryString(arrayOf(fields))

        return method.connect {
            val status = httpClient.executeMethod(method)
            if (status == 200) {
                logger.debug("Successfully got accessible projects in AdminRestClient: code $status")
                val json: JsonArray = JsonParser.parseReader(method.responseBodyAsReader) as JsonArray
                json.map { it.asJsonObject.get("shortName").asString }
            } else {
                logger.debug("Runtime Exception for fetching accessible projects in AdminRestClient," +
                        " code $status: ${method.responseBodyAsLoggedString()}")
                throw RuntimeException(method.responseBodyAsLoggedString())
            }
        }
    }
}