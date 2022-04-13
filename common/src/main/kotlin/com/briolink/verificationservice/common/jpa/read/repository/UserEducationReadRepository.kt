package com.briolink.verificationservice.common.jpa.read.repository

import com.briolink.verificationservice.common.jpa.read.entity.UserEducationReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserEducationReadRepository : JpaRepository<UserEducationReadEntity, UUID>
