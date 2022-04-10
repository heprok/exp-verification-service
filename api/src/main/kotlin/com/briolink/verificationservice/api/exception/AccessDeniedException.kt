package com.briolink.verificationservice.api.exception

class AccessDeniedException(override val message: String = "Access denied") : RuntimeException(message)
