package com.briolink.expverificationservice.common.jpa.read.repository

import com.briolink.expverificationservice.common.jpa.read.entity.CompanyReadEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface CompanyReadRepository : JpaRepository<CompanyReadEntity, UUID>
