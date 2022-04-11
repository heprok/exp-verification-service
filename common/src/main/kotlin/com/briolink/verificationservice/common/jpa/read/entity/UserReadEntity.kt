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
class UserReadEntity(

    @Id
    @Type(type = "pg-uuid")
    @Column(name = "id")
    var id: UUID

) : BaseReadEntity() {

    @Type(type = "jsonb")
    @Column(name = "data")
    lateinit var data: UserData

    data class UserData(
        @JsonProperty
        val id: UUID,
        @JsonProperty
        val slug: String,
        @JsonProperty
        val image: URL?,
        @JsonProperty
        val firstName: String,
        @JsonProperty
        val lastName: String,
    )
}
