package com.briolink.verificationservice.api.mapper

import com.briolink.verificationservice.api.types.VerificationCreatedResult
import com.briolink.verificationservice.api.types.VerificationStatus
import com.briolink.verificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.verificationservice.common.jpa.write.entity.VerificationWriteEntity
import com.briolink.verificationservice.common.mapper.toEnum

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
