package com.briolink.expverificationservice.common.jpa.write.repository

import com.briolink.expverificationservice.common.jpa.write.entity.VerificationWriteEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface VerificationWriteRepository : JpaRepository<VerificationWriteEntity, UUID> {

    fun existsByUserIdAndObjectConfirmIdAndObjectConfirmTypeIdAndStatusId(
        userId: UUID,
        objectConfirmId: UUID,
        typeId: Int,
        statusId: Int
    ): Boolean
}
