package com.briolink.expverificationservice.common.jpa.write.repository

import com.briolink.expverificationservice.common.jpa.write.entity.VerificationStatusWriteEntity
import org.springframework.data.jpa.repository.JpaRepository

interface VerificationStatusWriteRepository : JpaRepository<VerificationStatusWriteEntity, Int>
