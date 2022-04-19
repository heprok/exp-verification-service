package com.briolink.expverificationservice.common.jpa.write.repository

import com.briolink.expverificationservice.common.jpa.write.entity.VerificationWriteEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface VerificationWriteRepository : JpaRepository<VerificationWriteEntity, UUID> {

    fun existsByUserIdAndObjectConfirmIdAndObjectConfirmTypeIdAndStatusId(
        userId: UUID,
        objectConfirmId: UUID,
        typeId: Int,
        statusId: Int
    ): Boolean
}
