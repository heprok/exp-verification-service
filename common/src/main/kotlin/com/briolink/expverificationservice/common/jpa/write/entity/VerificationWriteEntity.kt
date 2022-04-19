package com.briolink.expverificationservice.common.jpa.write.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.Type
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID
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

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "status_id", nullable = false)
    lateinit var status: VerificationStatusWriteEntity

    @Type(type = "pg-uuid")
    @Column(name = "action_by")
    var actionBy: UUID? = null

    @Column(name = "action_at")
    var actionAt: Instant? = null

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    lateinit var created: Instant

    @UpdateTimestamp
    @Column(name = "changed")
    var changed: Instant? = null
}
