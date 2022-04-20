package com.briolink.expverificationservice.api.mapper

import com.briolink.expverificationservice.api.types.ConfirmData
import com.briolink.expverificationservice.api.types.User
import com.briolink.expverificationservice.api.types.Verification
import com.briolink.expverificationservice.api.types.VerificationCreatedResult
import com.briolink.expverificationservice.api.types.VerificationStatus
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.entity.verification.EducationVerificationReadEntity
import com.briolink.expverificationservice.common.jpa.read.entity.verification.WorkExperienceVerificationReadEntity
import com.briolink.expverificationservice.common.jpa.write.entity.VerificationWriteEntity
import com.briolink.expverificationservice.common.mapper.toEnum

fun VerificationCreatedResult.Companion.fromWrite(entity: VerificationWriteEntity) = VerificationCreatedResult(
    id = entity.id.toString(),
    status = VerificationStatus.fromEnum(entity.status.toEnum())
)

fun VerificationStatus.Companion.fromEnum(enum: VerificationStatusEnum): VerificationStatus =
    when (enum) {
        VerificationStatusEnum.NotConfirmed -> VerificationStatus.NotConfirmed
        VerificationStatusEnum.Pending -> VerificationStatus.Pending
        VerificationStatusEnum.Confirmed -> VerificationStatus.Confirmed
        VerificationStatusEnum.Rejected -> VerificationStatus.Rejected
    }

fun Verification.Companion.fromEducationVerificationRead(entity: EducationVerificationReadEntity) = Verification(
    id = entity.id.toString(),
    user = User.fromUserData(entity.userData),
    confirmData = ConfirmData.fromUserEducationData(entity.userEducationData),
)

fun Verification.Companion.fromWorkExpVerificationRead(entity: WorkExperienceVerificationReadEntity) = Verification(
    id = entity.id.toString(),
    user = User.fromUserData(entity.userData),
    confirmData = ConfirmData.fromUserJobPositionData(entity.userJobPositionData),
)
