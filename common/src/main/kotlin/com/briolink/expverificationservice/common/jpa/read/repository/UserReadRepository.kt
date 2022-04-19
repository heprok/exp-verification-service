package com.briolink.expverificationservice.common.jpa.read.repository

import com.briolink.expverificationservice.common.jpa.read.entity.UserReadEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface UserReadRepository : JpaRepository<UserReadEntity, UUID>
