package com.briolink.verificationservice.common.mapper

import com.briolink.verificationservice.common.domain.v1_0.ObjectConfirmType
import com.briolink.verificationservice.common.domain.v1_0.Verification
import com.briolink.verificationservice.common.domain.v1_0.VerificationStatus
import com.briolink.verificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.verificationservice.common.jpa.write.entity.VerificationStatusWriteEntity
import com.briolink.verificationservice.common.jpa.write.entity.VerificationWriteEntity

fun VerificationWriteEntity.toDomain(): Verification {
    return Verification(
        id = id!!,
        userId = userId,
        objectConfirmId = objectConfirmId,
        objectConfirmType = objectConfirmType.let { ObjectConfirmType.fromInt(it.id!!) },
        userToConfirmIds = ArrayList(userToConfirmIds.toList()),
        actionBy = actionBy,
        status = status.let { VerificationStatus.valueOf(it.name) },
        actionAt = actionAt,
        created = created,
        changed = changed
    )
}

fun VerificationStatusWriteEntity.toEnum(): VerificationStatusEnum = VerificationStatusEnum.ofValue(id!!)
