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
@Table(name = "user_job_position", schema = "read")
class UserJobPositionReadEntity(
    @Id
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    var id: UUID
) : BaseReadEntity() {

    @Type(type = "pg-uuid")
    @Column(name = "company_id", nullable = false)
    lateinit var companyId: UUID

    @Column(name = "data", nullable = false)
    @Type(type = "jsonb")
    lateinit var data: UserJobPositionData

    data class UserJobPositionData(
        @JsonProperty
        var id: UUID,
        @JsonProperty
        var company: CompanyReadEntity.CompanyData,
        @JsonProperty
        var title: String,
        @JsonProperty
        var startDate: LocalDate,
        @JsonProperty
        var endDate: LocalDate? = null
    )
}
