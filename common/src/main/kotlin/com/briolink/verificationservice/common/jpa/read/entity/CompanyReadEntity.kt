package com.briolink.verificationservice.common.jpa.read.entity

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import java.net.URL
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "company", schema = "read")
class CompanyReadEntity(
    @Id
    @Type(type = "pg-uuid")
    @Column(name = "id", nullable = false)
    var id: UUID
) : BaseReadEntity() {

    @Type(type = "jsonb")
    @Column(name = "data", nullable = false)
    lateinit var data: CompanyData

    data class CompanyData(
        @JsonProperty
        val id: UUID,
        @JsonProperty
        val name: String,
        @JsonProperty
        val slug: String,
        @JsonProperty
        val logo: URL? = null
    )
}
