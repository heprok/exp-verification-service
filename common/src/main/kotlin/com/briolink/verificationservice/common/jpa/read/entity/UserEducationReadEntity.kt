package com.briolink.verificationservice.common.jpa.read.entity

import com.briolink.verificationservice.common.domain.v1_0.VerificationStatus
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user_education", schema = "read")
class UserEducationReadEntity(
    @Id
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    var id: UUID
) : BaseReadEntity() {

    @Type(type = "pg-uuid")
    @Column(name = "university_id", nullable = false)
    lateinit var universityId: UUID

    @Type(type = "pg-uuid")
    @Column(name = "user_id", nullable = false)
    lateinit var userId: UUID

    @Column(name = "status", nullable = false)
    private var _status: Int = VerificationStatus.NotConfirmed.value

    var status: VerificationStatus
        get() = VerificationStatus.fromInt(_status)
        set(value) {
            _status = value.value
        }

    @Column(name = "data", nullable = false)
    @Type(type = "jsonb")
    lateinit var data: UserEducationData

    data class UserEducationData(
        @JsonProperty
        val id: UUID,
        @JsonProperty
        var university: UniversityReadEntity.UniversityData,
        @JsonProperty
        var degree: String,
        @JsonProperty
        var startDate: LocalDate,
        @JsonProperty
        var endDate: LocalDate? = null
    )
}
