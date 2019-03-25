package no.uio.ifi.jenhub

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@SpringBootApplication
class Application {

    @LoadBalanced
    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
