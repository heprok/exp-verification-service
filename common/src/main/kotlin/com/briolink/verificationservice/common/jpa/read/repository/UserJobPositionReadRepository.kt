package com.briolink.verificationservice.common.jpa.read.repository;

import com.briolink.verificationservice.common.jpa.read.entity.UserJobPositionReadEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface UserJobPositionReadRepository : JpaRepository<UserJobPositionReadEntity, UUID> {
}
