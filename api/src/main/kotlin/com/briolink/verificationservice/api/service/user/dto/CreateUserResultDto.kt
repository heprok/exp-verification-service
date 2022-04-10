package com.briolink.verificationservice.api.service.user.dto

import java.util.UUID

data class CreateUserResultDto(
    val id: UUID,
    val registered: Boolean,
    val slug: String,
    val personalEmail: String
)
