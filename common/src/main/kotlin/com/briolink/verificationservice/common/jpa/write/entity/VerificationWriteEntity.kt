package com.briolink.verificationservice.common.jpa.write.entity

import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.UpdateTimestamp
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "verification", schema = "write")
class VerificationWriteEntity : BaseWriteEntity() {

    @Type(type = "pg-uuid")
    @Column(name = "user_id", nullable = false)
    lateinit var userId: UUID

    @Type(type = "pg-uuid")
    @Column(name = "object_confirm_id", nullable = false)
    lateinit var objectConfirmId: UUID

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "object_confirm_type_id", nullable = false)
    lateinit var objectConfirmType: ObjectConfirmTypeWriteEntity

    @Type(type = "uuid-array")
    @Column(name = "user_to_confirm_ids", columnDefinition = "uuid[]", nullable = false)
    lateinit var userToConfirmIds: Array<UUID>

    @Type(type = "pg-uuid")
    @Column(name = "reject_by", nullable = false)
    var rejectBy: UUID? = null

    @Type(type = "pg-uuid")
    @Column(name = "confirm_by", nullable = false)
    var confirmBy: UUID? = null

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    lateinit var created: Instant

    @UpdateTimestamp
    @Column(name = "changed")
    var changed: Instant? = null
}
