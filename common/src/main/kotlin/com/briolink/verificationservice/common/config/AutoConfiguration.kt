package com.briolink.verificationservice.common.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.briolink.verificationservice.common.types",
        "com.briolink.verificationservice.common.config"
    ]
)
class AutoConfiguration
