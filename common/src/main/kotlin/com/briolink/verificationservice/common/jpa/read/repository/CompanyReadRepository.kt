package com.briolink.verificationservice.common.jpa.read.repository

import com.briolink.verificationservice.common.jpa.read.entity.CompanyReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CompanyReadRepository : JpaRepository<CompanyReadEntity, UUID>
