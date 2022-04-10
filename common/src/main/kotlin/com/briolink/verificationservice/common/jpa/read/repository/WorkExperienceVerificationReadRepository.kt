package com.briolink.verificationservice.common.jpa.read.repository;

import com.briolink.verificationservice.common.jpa.read.entity.verification.WorkExperienceVerificationReadEntity
import com.briolink.verificationservice.common.jpa.read.entity.verification.WorkExperienceVerificationReadEntityId
import org.springframework.data.jpa.repository.JpaRepository

interface WorkExperienceVerificationReadRepository :
    JpaRepository<WorkExperienceVerificationReadEntity, WorkExperienceVerificationReadEntityId> {
}
