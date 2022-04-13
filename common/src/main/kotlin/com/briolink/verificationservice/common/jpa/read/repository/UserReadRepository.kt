package com.briolink.verificationservice.common.jpa.read.repository

import com.briolink.verificationservice.common.jpa.read.entity.UserReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserReadRepository : JpaRepository<UserReadEntity, UUID>
