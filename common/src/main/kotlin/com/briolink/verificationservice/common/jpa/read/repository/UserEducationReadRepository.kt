package com.briolink.verificationservice.common.jpa.read.repository;

import com.briolink.verificationservice.common.jpa.read.entity.UserEducationReadEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface UserEducationReadRepository : JpaRepository<UserEducationReadEntity, UUID> {
}
