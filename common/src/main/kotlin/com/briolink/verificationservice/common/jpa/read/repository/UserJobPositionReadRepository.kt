package com.briolink.verificationservice.common.jpa.read.repository

import com.briolink.verificationservice.common.jpa.read.entity.UserJobPositionReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface UserJobPositionReadRepository : JpaRepository<UserJobPositionReadEntity, UUID> {

    @Query("select (count(u) > 0) from UserJobPositionReadEntity u where u.id = ?1 and u.userId = ?2 and u._status = ?3")
    fun existsByIdAndUserIdAndStatus(id: UUID, userId: UUID, status: Int): Boolean
}
