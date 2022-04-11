package com.briolink.verificationservice.common.jpa.read.repository;

import com.briolink.verificationservice.common.jpa.read.entity.UniversityReadEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface UniversityReadRepository : JpaRepository<UniversityReadEntity, UUID> {
}
