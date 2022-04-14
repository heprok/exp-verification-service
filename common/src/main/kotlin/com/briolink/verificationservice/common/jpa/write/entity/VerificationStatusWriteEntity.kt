package com.briolink.verificationservice.common.jpa.write.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "verification_status", schema = "write")
@Entity
class VerificationStatusWriteEntity {
    @Id
    var id: Int? = null

    @Column(name = "name", nullable = false, length = 30)
    lateinit var name: String
}
