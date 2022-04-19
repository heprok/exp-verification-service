package com.briolink.expverificationservice.api.exception

class UnavailableException(override val message: String = "Service unavailable") : RuntimeException(message)
