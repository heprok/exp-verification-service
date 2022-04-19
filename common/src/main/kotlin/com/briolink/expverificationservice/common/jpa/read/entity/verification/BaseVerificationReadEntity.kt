package com.briolink.expverificationservice.common.jpa.read.entity.verification

import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.entity.UserReadEntity
import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.search.PostgreSQLTSVectorType
import java.time.Instant
import java.util.UUID
import org.hibernate.annotations.ColumnTransformer
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import javax.persistence.Column
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

    @Column(name = "status", nullable = false)
    private var _status: Int = VerificationStatusEnum.NotConfirmed.value

    var status: VerificationStatusEnum
        get() = VerificationStatusEnum.ofValue(_status)
        set(value) {
            _status = value.value
        }

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
