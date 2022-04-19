package com.briolink.expverificationservice.common.jpa.read.entity.verification

import com.briolink.expverificationservice.common.jpa.read.entity.UserEducationReadEntity
import org.hibernate.annotations.ColumnTransformer
import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Table

@Entity
@Table(name = "education_verification", schema = "read")
class EducationVerificationReadEntity() : BaseVerificationReadEntity() {

    @Type(type = "pg-uuid")
    @Column(name = "user_education_id")
    lateinit var userEducationId: UUID

    @Type(type = "pg-uuid")
    @Column(name = "university_id", nullable = false)
    lateinit var universityId: UUID

    @Column(name = "university_name", nullable = false)
    lateinit var universityName: String

    @ColumnTransformer(write = "to_tsvector('simple', ?)")
    @Column(name = "university_name_tsv", nullable = false)
    private lateinit var universityNameTsv: String

    @Column(name = "degree", nullable = false)
    lateinit var degree: String

    @ColumnTransformer(write = "to_tsvector('simple', ?)")
    @Column(name = "degree_tsv", nullable = false)
    private lateinit var degreeTsv: String

    @Type(type = "jsonb")
    @Column(name = "data", nullable = false)
    lateinit var userEducationData: UserEducationReadEntity.UserEducationData

    @PrePersist
    @PreUpdate
    fun tsvUpdate() {
        universityNameTsv = universityName
        degreeTsv = degree
        userFullNameTsv = userFullName
    }
}
