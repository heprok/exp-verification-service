package com.briolink.verificationservice.common.jpa.read.repository;

import com.briolink.verificationservice.common.jpa.read.entity.verification.EducationVerificationReadEntity
import com.briolink.verificationservice.common.jpa.read.entity.verification.EducationVerificationReadEntityId
import org.springframework.data.jpa.repository.JpaRepository

interface EducationVerificationReadRepository : JpaRepository<EducationVerificationReadEntity, EducationVerificationReadEntityId> {
}