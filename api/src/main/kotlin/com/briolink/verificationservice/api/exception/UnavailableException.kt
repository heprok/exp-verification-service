package com.briolink.verificationservice.api.exception

class UnavailableException(override val message: String = "Service unavailable") : RuntimeException(message)
