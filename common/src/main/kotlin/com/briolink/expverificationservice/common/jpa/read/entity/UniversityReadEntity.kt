package com.briolink.expverificationservice.common.jpa.read.entity

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.annotations.Type
import java.net.URL
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "university", schema = "read")
class UniversityReadEntity(
    @Id
    @Column(name = "id", nullable = false)
    @Type(type = "pg-uuid")
    var id: UUID
) : BaseReadEntity() {

    @Column(name = "data", nullable = false)
    @Type(type = "jsonb")
    lateinit var data: UniversityData

    data class UniversityData(
        @JsonProperty
        val id: UUID,
        @JsonProperty
        val name: String,
        @JsonProperty
        val logo: URL?
    )
}
