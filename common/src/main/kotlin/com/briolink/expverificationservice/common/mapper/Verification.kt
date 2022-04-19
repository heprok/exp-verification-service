package com.briolink.expverificationservice.common.mapper

import com.briolink.expverificationservice.common.domain.v1_0.ObjectConfirmType
import com.briolink.expverificationservice.common.domain.v1_0.Verification
import com.briolink.expverificationservice.common.domain.v1_0.VerificationStatus
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.write.entity.VerificationStatusWriteEntity
import com.briolink.expverificationservice.common.jpa.write.entity.VerificationWriteEntity

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
