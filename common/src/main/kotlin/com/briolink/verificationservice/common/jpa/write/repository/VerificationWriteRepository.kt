package com.briolink.verificationservice.common.jpa.write.repository

import com.briolink.verificationservice.common.jpa.write.entity.VerificationWriteEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface VerificationWriteRepository : JpaRepository<VerificationWriteEntity, UUID>
