package com.briolink.expverificationservice.api.service.verifcation.education.dto

import com.briolink.expverificationservice.api.service.verifcation.VerificationListRequest
import java.util.UUID

data class EducationVerificationListRequest(
    var filters: EducationVerificationListFilter? = null,
    override var offset: Int = 0,
    override var limit: Int = 10,
) : VerificationListRequest

data class EducationVerificationListFilter(
    val universityIds: List<UUID>? = null,
    val userFullNames: List<String>? = null,
    val degrees: List<String>? = null,
)
