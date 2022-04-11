package com.briolink.verificationservice.common.jpa.read.entity

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
class UserEducationReadEntity : BaseReadEntity() {
    @Id
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    lateinit var id: UUID

    @Type(type = "pg-uuid")
    @Column(name = "university_id", nullable = false)
    lateinit var universityId: UUID

    @Column(name = "data", nullable = false)
    @Type(type = "jsonb")
    lateinit var data: UserEducationData

    data class UserEducationData(
        @JsonProperty
        val id: UUID,
        @JsonProperty
        val university: UniversityReadEntity.UniversityData,
        @JsonProperty
        val degree: String,
        @JsonProperty
        val startDate: LocalDate,
        @JsonProperty
        val endDate: LocalDate? = null
    )
}
