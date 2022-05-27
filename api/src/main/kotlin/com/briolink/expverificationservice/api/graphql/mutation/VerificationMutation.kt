package com.briolink.expverificationservice.api.graphql.mutation

import com.briolink.expverificationservice.api.mapper.fromEnum
import com.briolink.expverificationservice.api.mapper.fromWrite
import com.briolink.expverificationservice.api.service.verifcation.education.EducationVerificationService
import com.briolink.expverificationservice.api.service.verifcation.workexperince.WorkExperienceVerificationService
import com.briolink.expverificationservice.api.types.ConfirmVerificationResult
import com.briolink.expverificationservice.api.types.Error
import com.briolink.expverificationservice.api.types.ObjectConfirmType
import com.briolink.expverificationservice.api.types.ObjectKey
import com.briolink.expverificationservice.api.types.ResetVerificationResult
import com.briolink.expverificationservice.api.types.VerificationConfirmAction
import com.briolink.expverificationservice.api.types.VerificationCreatedResult
import com.briolink.expverificationservice.api.types.VerificationRequestResult
import com.briolink.expverificationservice.api.types.VerificationStatus
import com.briolink.expverificationservice.common.enumeration.ActionTypeEnum
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.lib.common.utils.BlSecurityUtils
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.InputArgument
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import org.springframework.security.access.prepost.PreAuthorize
import java.util.UUID
import javax.xml.bind.ValidationException

@DgsComponent
class VerificationMutation(
    private val workExperienceVerificationService: WorkExperienceVerificationService,
    private val educationVerificationService: EducationVerificationService,
) {
    @DgsMutation
    @PreAuthorize("@blServletUtils.isIntranet()")
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
        } catch (ex: ValidationException) {
            VerificationRequestResult(
                userErrors = listOf(Error(ex.message ?: ""))
            )
        } catch (ex: DgsEntityNotFoundException) {
            VerificationRequestResult(
                userErrors = listOf(Error(ex.message))
            )
        }
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated()")
    fun confirmVerification(
        @InputArgument id: String,
        @InputArgument type: ObjectConfirmType,
        @InputArgument action: VerificationConfirmAction,
    ): ConfirmVerificationResult {
        return try {
            when (action) {
                VerificationConfirmAction.Confirm -> when (type) {
                    ObjectConfirmType.WorkExperience -> workExperienceVerificationService.confirmVerification(
                        id = UUID.fromString(id),
                        byUserId = BlSecurityUtils.currentUserId,
                        actionType = ActionTypeEnum.Confirmed
                    )
                    ObjectConfirmType.Education -> educationVerificationService.confirmVerification(
                        id = UUID.fromString(id),
                        byUserId = BlSecurityUtils.currentUserId,
                        actionType = ActionTypeEnum.Confirmed
                    )
                }
                VerificationConfirmAction.Reject -> when (type) {
                    ObjectConfirmType.WorkExperience -> workExperienceVerificationService.confirmVerification(
                        id = UUID.fromString(id),
                        byUserId = BlSecurityUtils.currentUserId,
                        actionType = ActionTypeEnum.Rejected
                    )
                    ObjectConfirmType.Education -> educationVerificationService.confirmVerification(
                        id = UUID.fromString(id),
                        byUserId = BlSecurityUtils.currentUserId,
                        actionType = ActionTypeEnum.Rejected
                    )
                }
            }

            ConfirmVerificationResult(
                success = true,
                userErrors = listOf()
            )
        } catch (ex: ValidationException) {
            ConfirmVerificationResult(
                success = false,
                userErrors = listOf(Error(ex.message ?: ""))
            )
        } catch (ex: DgsEntityNotFoundException) {
            ConfirmVerificationResult(
                success = false,
                userErrors = listOf(Error(ex.message))
            )
        }
    }

    @DgsMutation
    @PreAuthorize("@blServletUtils.isIntranet()")
    fun confirmWorkExpVerificationByCompany(
        @InputArgument id: String,
        @InputArgument byUserId: String,
        @InputArgument action: VerificationConfirmAction,
    ): ConfirmVerificationResult {
        return try {
            when (action) {
                VerificationConfirmAction.Confirm -> workExperienceVerificationService.confirmVerification(
                    id = UUID.fromString(id),
                    byUserId = UUID.fromString(byUserId),
                    actionType = ActionTypeEnum.Confirmed,
                    overrideAction = true
                )
                VerificationConfirmAction.Reject -> workExperienceVerificationService.confirmVerification(
                    id = UUID.fromString(id),
                    byUserId = UUID.fromString(byUserId),
                    actionType = ActionTypeEnum.Rejected,
                    overrideAction = true
                )
            }

            ConfirmVerificationResult(
                success = true,
                userErrors = listOf()
            )
        } catch (ex: ValidationException) {
            ConfirmVerificationResult(
                success = false,
                userErrors = listOf(Error(ex.message ?: ""))
            )
        } catch (ex: DgsEntityNotFoundException) {
            ConfirmVerificationResult(
                success = false,
                userErrors = listOf(Error(ex.message))
            )
        }
    }

    @DgsMutation
    @PreAuthorize("@blServletUtils.isIntranet()")
    fun resetVerification(
        @InputArgument objectKey: ObjectKey,
        @InputArgument(collectionType = VerificationStatus::class) overrideStatus: VerificationStatus?
    ): ResetVerificationResult {
        println(overrideStatus)
        val status = when (objectKey.type) {
            ObjectConfirmType.WorkExperience -> workExperienceVerificationService.resetObjectVerification(
                objectId = UUID.fromString(objectKey.id),
                overrideStatus = overrideStatus?.name?.let { VerificationStatusEnum.valueOf(it) }
            )
            ObjectConfirmType.Education -> educationVerificationService.resetObjectVerification(
                objectId = UUID.fromString(objectKey.id),
                overrideStatus = overrideStatus?.name?.let { VerificationStatusEnum.valueOf(it) }
            )
        }
        return ResetVerificationResult(success = true, status = VerificationStatus.fromEnum(status))
    }
}
