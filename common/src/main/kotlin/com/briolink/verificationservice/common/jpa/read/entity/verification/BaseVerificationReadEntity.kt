package com.briolink.verificationservice.common.jpa.read.entity.verification

import com.briolink.verificationservice.common.enumeration.ActionTypeEnum
import com.briolink.verificationservice.common.jpa.read.entity.UserReadEntity
import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.search.PostgreSQLTSVectorType
import org.hibernate.annotations.ColumnTransformer
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@TypeDefs(
    TypeDef(name = "jsonb", typeClass = JsonBinaryType::class),
    TypeDef(name = "uuid-array", typeClass = UUIDArrayType::class),
    TypeDef(name = "tsvector", typeClass = PostgreSQLTSVectorType::class)
)
@MappedSuperclass
abstract class BaseVerificationReadEntity {

    @Id
    @Type(type = "pg-uuid")
    @Column(name = "id", nullable = false)
    lateinit var id: UUID

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

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    var actionType: ActionTypeEnum? = null

    @Type(type = "pg-uuid")
    @Column(name = "action_by")
    var actionBy: UUID? = null

    @Column(name = "action_at")
    var actionAt: Instant? = null

    @Column(name = "created", nullable = false)
    lateinit var created: Instant

    @Column(name = "changed")
    var changed: Instant? = null

    @Type(type = "jsonb")
    @Column(name = "user_data", columnDefinition = "jsonb", nullable = false)
    lateinit var userData: UserReadEntity.UserData
}
