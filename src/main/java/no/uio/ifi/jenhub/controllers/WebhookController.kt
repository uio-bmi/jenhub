package no.uio.ifi.jenhub.controllers

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class WebhookController(
    private val restTemplate: RestTemplate
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
        restTemplate.exchange("http://jenkins/jenkins", HttpMethod.POST, entity, Unit.javaClass)
    }

    private fun prepareJSON(service: String, image: Any?, tag: Any?) =
        "{ \"service\" : \"$service\", \"image\" : \"$image:$tag\" }"

}
