package com.briolink.verificationservice.api.exception

class UserErrorGraphQlException(override val message: String) : RuntimeException(message)
