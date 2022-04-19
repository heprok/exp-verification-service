package com.briolink.expverificationservice.api.service.verifcation.workexperince.dto

import com.briolink.expverificationservice.api.service.verifcation.VerificationListRequest
import java.util.UUID

data class WorkExpVerificationListRequest(
    var filters: WorkExpVerificationListFilter? = null,
    override var offset: Int = 0,
    override var limit: Int = 10,
) : VerificationListRequest

data class WorkExpVerificationListFilter(
    val companyIds: List<UUID>? = null,
    val userFullNames: List<String>? = null,
    val positionTitles: List<String>? = null,
)
