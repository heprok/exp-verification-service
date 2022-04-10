package com.briolink.verificationservice.common.config

import org.jetbrains.annotations.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.app-endpoints")
class AppEndpointsProperties {
    @NotNull
    lateinit var user: String
}
