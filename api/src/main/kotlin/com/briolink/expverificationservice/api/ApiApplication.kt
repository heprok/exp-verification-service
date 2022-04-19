package com.briolink.expverificationservice.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan(
    basePackages = [
        "com.briolink.expverificationservice.common.jpa.read.entity",
        "com.briolink.expverificationservice.common.jpa.write.entity",
    ],
)
@EnableJpaRepositories(
    basePackages = [
        "com.briolink.expverificationservice.common.jpa.read.repository",
        "com.briolink.expverificationservice.common.jpa.write.repository",
    ],
)
@ConfigurationPropertiesScan
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
