package com.briolink.expverificationservice.common.jpa.write.entity

import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import java.util.UUID
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@TypeDefs(
    TypeDef(name = "uuid-array", typeClass = UUIDArrayType::class),
)
@MappedSuperclass
abstract class BaseWriteEntity {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    var id: UUID? = null
}
