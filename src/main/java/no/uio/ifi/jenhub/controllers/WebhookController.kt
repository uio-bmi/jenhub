package no.uio.ifi.jenhub.controllers

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class WebhookController(
    private val restTemplate: RestTemplate,
    @Value("\${jenkins.ssl}") private val jenkinsSSL: Boolean,
    @Value("\${jenkins.token}") private val jenkinsToken: String
) {

    @PostMapping("/{service}")
    fun webhook(
        @PathVariable service: String,
        @RequestBody payload: Map<*, *>
    ) {
        val tag = (payload["push_data"] as Map<*, *>)["tag"]
        val image = (payload["repository"] as Map<*, *>)["repo_name"]
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON_UTF8
        val entity = HttpEntity(prepareJSON(service, image, tag), headers)
        restTemplate.postForEntity(
            "${if (jenkinsSSL) "https" else "http"}://jenkins/generic-webhook-trigger/invoke?token={token}",
            entity,
            Unit.javaClass,
            jenkinsToken
        )
    }

    private fun prepareJSON(service: String, image: Any?, tag: Any?) =
        "{ \"service\" : \"$service\", \"image\" : \"$image:$tag\" }"

}
