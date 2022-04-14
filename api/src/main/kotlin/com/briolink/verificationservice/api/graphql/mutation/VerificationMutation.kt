package com.briolink.verificationservice.api.graphql.mutation

import com.briolink.verificationservice.api.exception.UserErrorGraphQlException
import com.briolink.verificationservice.api.service.verifcation.workexperince.VerificationWorkExperienceService
import com.briolink.verificationservice.api.types.Error
import com.briolink.verificationservice.api.types.ObjectConfirmType
import com.briolink.verificationservice.api.types.ObjectKey
import com.briolink.verificationservice.api.types.VerificationConfirmAction
import com.briolink.verificationservice.api.types.VerificationRequestResult
import com.briolink.verificationservice.api.util.SecurityUtil
import com.briolink.verificationservice.common.enumeration.ActionTypeEnum
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.prepost.PreAuthorize
import java.util.UUID

@DgsComponent
class VerificationMutation(
    private val verificationWorkExperienceService: VerificationWorkExperienceService
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
                ObjectConfirmType.WorkExperience -> verificationWorkExperienceService.addVerification(
                    userId = UUID.fromString(byUserId),
                    objectId = UUID.fromString(objectKey.id),
                    userConfirmIds = usersToConfirmIds.map { UUID.fromString(it) }
                )
                ObjectConfirmType.Education -> throw UserErrorGraphQlException(
                    message = "Object type ${objectKey.type} is not supported yet",
                )
            }
            VerificationRequestResult(
                success = true,
                userErrors = listOf()
            )
        } catch (ex: UserErrorGraphQlException) {
            VerificationRequestResult(
                success = false,
                userErrors = listOf(Error(ex.message))
            )
        }
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated()")
    fun confirmVerification(
        @InputArgument id: String,
        @InputArgument action: VerificationConfirmAction,
    ): VerificationRequestResult {
        return try {
            when (action) {
                VerificationConfirmAction.Confirm -> verificationWorkExperienceService.confirmVerification(
                    id = UUID.fromString(id),
                    byUserId = SecurityUtil.currentUserId,
                    actionType = ActionTypeEnum.Confirmed
                )
                VerificationConfirmAction.Reject -> verificationWorkExperienceService.confirmVerification(
                    id = UUID.fromString(id),
                    byUserId = SecurityUtil.currentUserId,
                    actionType = ActionTypeEnum.Rejected
                )
            }

            VerificationRequestResult(
                success = true,
                userErrors = listOf()
            )
        } catch (ex: UserErrorGraphQlException) {
            VerificationRequestResult(
                success = false,
                userErrors = listOf(Error(ex.message))
            )
        }
    }
}
