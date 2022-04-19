package com.briolink.expverificationservice.common.jpa.write.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "object_confirm_type", schema = "write")
@Entity
class ObjectConfirmTypeWriteEntity {
    @Id
    var id: Int? = null

    @Column(name = "name", nullable = false, length = 30)
    lateinit var name: String
}
