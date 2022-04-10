package com.briolink.verificationservice.common.jpa.read.entity

import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import com.vladmihalcea.hibernate.type.range.PostgreSQLRangeType
import com.vladmihalcea.hibernate.type.range.Range
import com.vladmihalcea.hibernate.type.search.PostgreSQLTSVectorType
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import javax.persistence.MappedSuperclass

@TypeDefs(
    TypeDef(name = "jsonb", typeClass = JsonBinaryType::class),
    TypeDef(name = "uuid-array", typeClass = UUIDArrayType::class),
    TypeDef(name = "tsvector", typeClass = PostgreSQLTSVectorType::class),
    TypeDef(typeClass = PostgreSQLRangeType::class, defaultForType = Range::class),
)
@MappedSuperclass
abstract class BaseReadEntity
