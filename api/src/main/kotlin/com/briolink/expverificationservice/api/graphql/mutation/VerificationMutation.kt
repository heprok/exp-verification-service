package com.briolink.expverificationservice.api.graphql.mutation

import com.briolink.expverificationservice.api.exception.UserErrorGraphQlException
import com.briolink.expverificationservice.api.mapper.fromWrite
import com.briolink.expverificationservice.api.service.verifcation.education.EducationVerificationService
import com.briolink.expverificationservice.api.service.verifcation.workexperince.WorkExperienceVerificationService
import com.briolink.expverificationservice.api.types.ConfirmVerificationResult
import com.briolink.expverificationservice.api.types.Error
import com.briolink.expverificationservice.api.types.ObjectConfirmType
import com.briolink.expverificationservice.api.types.ObjectKey
import com.briolink.expverificationservice.api.types.VerificationConfirmAction
import com.briolink.expverificationservice.api.types.VerificationCreatedResult
import com.briolink.expverificationservice.api.types.VerificationRequestResult
import com.briolink.expverificationservice.api.util.SecurityUtil
import com.briolink.expverificationservice.common.enumeration.ActionTypeEnum
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.prepost.PreAuthorize
import java.util.UUID

@DgsComponent
class VerificationMutation(
    private val workExperienceVerificationService: WorkExperienceVerificationService,
    private val educationVerificationService: EducationVerificationService,
) {
    @DgsMutation
    @PreAuthorize("@servletUtil.isIntranet()")
    fun verificationRequest(
        @InputArgument byUserId: String,
        @InputArgument objectKey: ObjectKey,
        @InputArgument usersToConfirmIds: List<String>
    ): VerificationRequestResult {
        return try {
            when (objectKey.type) {
                ObjectConfirmType.WorkExperience -> workExperienceVerificationService.addVerification(
                    userId = UUID.fromString(byUserId),
                    objectId = UUID.fromString(objectKey.id),
                    userConfirmIds = usersToConfirmIds.map { UUID.fromString(it) }
                )
                ObjectConfirmType.Education -> educationVerificationService.addVerification(
                    userId = UUID.fromString(byUserId),
                    objectId = UUID.fromString(objectKey.id),
                    userConfirmIds = usersToConfirmIds.map { UUID.fromString(it) }
                )
            }.let {
                VerificationRequestResult(
                    VerificationCreatedResult.fromWrite(it),
                    userErrors = listOf()
                )
            }
        } catch (ex: UserErrorGraphQlException) {
            VerificationRequestResult(
                userErrors = listOf(Error(ex.message))
            )
        }
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated()")
    fun confirmVerification(
        @InputArgument id: String,
        @InputArgument action: VerificationConfirmAction,
    ): ConfirmVerificationResult {
        return try {
            when (action) {
                VerificationConfirmAction.Confirm -> workExperienceVerificationService.confirmVerification(
                    id = UUID.fromString(id),
                    byUserId = SecurityUtil.currentUserId,
                    actionType = ActionTypeEnum.Confirmed
                )
                VerificationConfirmAction.Reject -> workExperienceVerificationService.confirmVerification(
                    id = UUID.fromString(id),
                    byUserId = SecurityUtil.currentUserId,
                    actionType = ActionTypeEnum.Rejected
                )
            }

            ConfirmVerificationResult(
                success = true,
                userErrors = listOf()
            )
        } catch (ex: UserErrorGraphQlException) {
            ConfirmVerificationResult(
                success = false,
                userErrors = listOf(com.briolink.expverificationservice.api.types.Error(ex.message))
            )
        }
    }
}
