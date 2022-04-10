package com.briolink.verificationservice.common.jpa.read.entity.verification

import com.briolink.verificationservice.common.types.ObjectConfirmId
import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.ColumnTransformer
import org.hibernate.annotations.Type
import java.net.URL
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Table

@Table(name = "work_experience_verification", schema = "read")
@Entity
class WorkExperienceVerificationReadEntity(
    @EmbeddedId
    var id: WorkExperienceVerificationReadEntityId
) : BaseVerificationReadEntity() {
    @Type(type = "pg-uuid")
    @Column(name = "company_id", nullable = false)
    lateinit var companyId: UUID

    @Column(name = "company_name", nullable = false)
    lateinit var companyName: String

    @ColumnTransformer(write = "to_tsvector('simple', ?)")
    @Column(name = "company_name_tsv", nullable = false)
    lateinit var companyNameTsv: String

    @Column(name = "job_position_title", nullable = false)
    lateinit var jobPositionTitle: String

    @ColumnTransformer(write = "to_tsvector('simple', ?)")
    @Column(name = "job_position_tsv", nullable = false)
    lateinit var jobPositionTsv: String

    @Type(type = "jsonb")
    @Column(name = "data", nullable = false)
    lateinit var userJobPositionData: UserJobPositionData

    @PrePersist
    @PreUpdate
    fun tsvUpdate() {
        companyNameTsv = companyName
        jobPositionTsv = jobPositionTitle
    }

    data class UserJobPositionData(
        @JsonProperty
        val id: UUID,
        @JsonProperty
        val company: Company,
        @JsonProperty
        val title: String,
        @JsonProperty
        val startDate: LocalDate,
        @JsonProperty
        val endDate: LocalDate? = null
    )

    data class Company(
        @JsonProperty
        val id: UUID,
        @JsonProperty
        val name: String,
        @JsonProperty
        val slug: String,
        @JsonProperty
        val logo: URL?
    )
}

@Embeddable
class WorkExperienceVerificationReadEntityId(
    objectId: ObjectConfirmId,
    @Column(name = "user_job_position_id", nullable = false, length = 36)
    override var objectConfirmId: UUID
) : VerificationReadEntityId(objectId)
