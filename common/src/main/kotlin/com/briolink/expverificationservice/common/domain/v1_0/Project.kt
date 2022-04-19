package com.briolink.expverificationservice.common.domain.v1_0

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.time.Year
import java.util.UUID

enum class ProjectStatus(val value: Int) {
    @JsonProperty("1")
    Draft(1),
    @JsonProperty("2")
    Pending(2),
    @JsonProperty("3")
    InProgress(3),
    @JsonProperty("4")
    Verified(4),
    @JsonProperty("5")
    Rejected(5);

    companion object {
        private val map = values().associateBy(ProjectStatus::value)
        fun fromInt(type: Int): ProjectStatus = map[type]!!
    }
}

enum class ProjectCompanyRoleType(val value: Int) {
    @JsonProperty("0")
    Buyer(0),
    @JsonProperty("1")
    Seller(1)
}

enum class ProjectObjectType(val value: Int) {
    @JsonProperty("1")
    User(1),
    @JsonProperty("2")
    Company(2),
    @JsonProperty("3")
    CompanyService(3)
}

data class ProjectCompanyRole(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val name: String,
    @JsonProperty
    val type: ProjectCompanyRoleType
)

data class ProjectService(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val serviceId: UUID? = null,
    @JsonProperty
    val serviceName: String,
    @JsonProperty
    val startDate: Year? = null,
    @JsonProperty
    val endDate: Year? = null,
) : Domain

data class ProjectParticipant(
    @JsonProperty
    val userId: UUID? = null,
    @JsonProperty
    val userJobPositionTitle: String? = null,
    @JsonProperty
    val companyId: UUID? = null,
    @JsonProperty
    val companyRole: ProjectCompanyRole? = null,
) : Domain

data class Project(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val participantFrom: ProjectParticipant,
    @JsonProperty
    val participantTo: ProjectParticipant,
    @JsonProperty
    val services: ArrayList<ProjectService> = arrayListOf(),
    @JsonProperty
    val previousStatus: ProjectStatus?,
    @JsonProperty
    val status: ProjectStatus,
    @JsonProperty
    val lastActionBy: Int,
    @JsonProperty
    val rejectReason: String?,
    @JsonProperty
    val rejectComment: String?,
    @JsonProperty
    var created: Instant,
    @JsonProperty
    var changed: Instant?,
    @JsonProperty
    var verified: Instant?
) : Domain

data class ProjectDeletedData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val objectType: ProjectObjectType,
    @JsonProperty
    val participantObjectId: UUID,
    @JsonProperty
    val anotherParticipantUserId: UUID,
    @JsonProperty
    val anotherParticipantCompanyId: UUID,
    @JsonProperty
    val serviceId: UUID?,
    @JsonProperty
    val completely: Boolean
) : Domain

data class ProjectVisibilityData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val serviceId: UUID?,
    @JsonProperty
    val objectType: ProjectObjectType,
    @JsonProperty
    val participantObjectId: UUID,
    @JsonProperty
    val anotherParticipantUserId: UUID,
    @JsonProperty
    val anotherParticipantCompanyId: UUID,
    @JsonProperty
    val hidden: Boolean
) : Domain

data class CompanyRole(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val name: String,
    @JsonProperty
    val type: ProjectCompanyRoleType
)
