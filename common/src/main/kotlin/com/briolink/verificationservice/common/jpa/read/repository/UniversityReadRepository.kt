package com.briolink.verificationservice.common.jpa.read.repository

import com.briolink.verificationservice.common.jpa.read.entity.UniversityReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UniversityReadRepository : JpaRepository<UniversityReadEntity, UUID>
