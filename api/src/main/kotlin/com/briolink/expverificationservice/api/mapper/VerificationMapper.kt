package com.briolink.expverificationservice.api.mapper

import com.briolink.expverificationservice.api.types.VerificationCreatedResult
import com.briolink.expverificationservice.api.types.VerificationStatus
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.write.entity.VerificationWriteEntity
import com.briolink.expverificationservice.common.mapper.toEnum

fun VerificationCreatedResult.Companion.fromWrite(entity: VerificationWriteEntity): VerificationCreatedResult {
    return VerificationCreatedResult(
        id = entity.id.toString(),
        status = VerificationStatus.fromEnum(entity.status.toEnum())
    )
}

fun VerificationStatus.Companion.fromEnum(enum: VerificationStatusEnum): VerificationStatus =
    when (enum) {
        VerificationStatusEnum.NotConfirmed -> VerificationStatus.NotConfirmed
        VerificationStatusEnum.Pending -> VerificationStatus.Pending
        VerificationStatusEnum.Confirmed -> VerificationStatus.Confirmed
        VerificationStatusEnum.Rejected -> VerificationStatus.Rejected
    }
