package com.briolink.expverificationservice.api.exception

class UserErrorGraphQlException(override val message: String) : RuntimeException(message)
