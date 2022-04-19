package com.briolink.expverificationservice.api.exception

class AccessDeniedException(override val message: String = "Access denied") : RuntimeException(message)
