package com.briolink.verificationservice.common.jpa.write.entity

import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.range.PostgreSQLRangeType
import com.vladmihalcea.hibernate.type.range.Range
import com.vladmihalcea.hibernate.type.search.PostgreSQLTSVectorType
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

