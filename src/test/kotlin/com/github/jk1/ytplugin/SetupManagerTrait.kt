package com.github.jk1.ytplugin

import com.github.jk1.ytplugin.rest.RestClientTrait
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.UsernamePasswordCredentials
import org.apache.commons.httpclient.auth.AuthScope

interface SetupManagerTrait : RestClientTrait, YouTrackConnectionTrait {

    override val httpClient: HttpClient
        get() {
            val client = HttpClient()
            client.params.connectionManagerTimeout = 30000 // ms
            client.params.soTimeout = 30000 // ms
            client.params.credentialCharset = "UTF-8"
            client.params.isAuthenticationPreemptive = true
            val credentials = UsernamePasswordCredentials(username, password)
            client.state.setCredentials(AuthScope.ANY, credentials)
            return client
        }
}