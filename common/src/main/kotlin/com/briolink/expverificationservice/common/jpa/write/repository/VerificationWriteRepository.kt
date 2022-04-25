package com.briolink.expverificationservice.common.jpa.write.repository

import com.briolink.expverificationservice.common.jpa.write.entity.ObjectConfirmTypeWriteEntity
import com.briolink.expverificationservice.common.jpa.write.entity.VerificationWriteEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface VerificationWriteRepository : JpaRepository<VerificationWriteEntity, UUID> {

    @Query(
        """
        SELECT v FROM VerificationWriteEntity v
        WHERE v.objectConfirmId = ?1 AND
            v.objectConfirmType = ?2
        ORDER BY v.changed DESC
        """
    )
    fun findFirstByObjectConfirmIdAndObjectConfirmTypeOrderByChangedDesc(
        objectConfirmId: UUID,
        objectConfirmType: ObjectConfirmTypeWriteEntity
    ): VerificationWriteEntity?
}
