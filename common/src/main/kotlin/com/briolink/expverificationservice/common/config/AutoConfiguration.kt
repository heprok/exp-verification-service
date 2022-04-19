package com.briolink.expverificationservice.common.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.briolink.expverificationservice.common.types",
        "com.briolink.expverificationservice.common.config"
    ]
)
class AutoConfiguration
