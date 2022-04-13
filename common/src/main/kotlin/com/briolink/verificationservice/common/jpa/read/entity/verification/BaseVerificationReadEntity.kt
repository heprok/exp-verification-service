package com.briolink.verificationservice.common.jpa.read.entity.verification

import com.briolink.verificationservice.common.jpa.read.entity.UserReadEntity
import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.search.PostgreSQLTSVectorType
import org.hibernate.annotations.ColumnTransformer
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.MappedSuperclass

@TypeDefs(
    TypeDef(name = "jsonb", typeClass = JsonBinaryType::class),
    TypeDef(name = "uuid-array", typeClass = UUIDArrayType::class),
    TypeDef(name = "tsvector", typeClass = PostgreSQLTSVectorType::class)
)
@MappedSuperclass
abstract class BaseVerificationReadEntity {
    @Type(type = "pg-uuid")
    @Column(name = "user_id", nullable = false)
    lateinit var userId: UUID

    @Column(name = "user_full_name", nullable = false)
    lateinit var userFullName: String

    @ColumnTransformer(write = "to_tsvector('simple', ?)")
    @Column(name = "user_full_name_tsv", nullable = false)
    lateinit var userFullNameTsv: String

    @Type(type = "uuid-array")
    @Column(name = "user_to_confirm_ids", columnDefinition = "uuid[]", nullable = false)
    lateinit var userToConfirmIds: Array<UUID>

    @Type(type = "pg-uuid")
    @Column(name = "reject_by")
    var rejectBy: UUID? = null

    @Type(type = "pg-uuid")
    @Column(name = "confirm_by")
    var confirmBy: UUID? = null

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    lateinit var created: Instant

    @UpdateTimestamp
    @Column(name = "changed")
    var changed: Instant? = null

    @Type(type = "jsonb")
    @Column(name = "user_data", columnDefinition = "jsonb", nullable = false)
    lateinit var userData: UserReadEntity.UserData
}
