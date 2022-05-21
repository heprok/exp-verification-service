package com.briolink.expverificationservice.api.service.suggestion

import com.briolink.expverificationservice.api.service.suggestion.dto.SuggestionGroupEnum
import com.briolink.expverificationservice.api.service.suggestion.dto.SuggestionTypeEnum
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.repository.EducationVerificationReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.WorkExperienceVerificationReadRepository
import com.briolink.lib.common.type.basic.BlSuggestion
import com.briolink.lib.common.utils.BlSecurityUtils
import org.springframework.stereotype.Service

@Service
class SuggestionService(
    private val educationVerificationReadRepository: EducationVerificationReadRepository,
    private val workExperienceVerificationReadRepository: WorkExperienceVerificationReadRepository,
) {
    fun getSuggestions(
        group: SuggestionGroupEnum,
        type: SuggestionTypeEnum,
        query: String? = null,
    ): List<BlSuggestion> {
        val q = if (query.isNullOrBlank()) null else query
        return if (type == SuggestionTypeEnum.UserFullName) {
            when (group) {
                SuggestionGroupEnum.Education -> educationVerificationReadRepository.getSuggestionUserFullNameAssociatedUser(
                    associatedUserId = BlSecurityUtils.currentUserId,
                    query = q,
                    status = VerificationStatusEnum.Pending.value
                )
                SuggestionGroupEnum.WorkExperience -> workExperienceVerificationReadRepository.getSuggestionUserFullNameAssociatedUser(
                    associatedUserId = BlSecurityUtils.currentUserId,
                    query = q,
                    status = VerificationStatusEnum.Pending.value
                )
            }
        } else if (group == SuggestionGroupEnum.WorkExperience) {
            when (type) {
                SuggestionTypeEnum.JobPosition -> workExperienceVerificationReadRepository.getSuggestionJobPositionAssociatedUser(
                    associatedUserId = BlSecurityUtils.currentUserId,
                    query = q,
                    status = VerificationStatusEnum.Pending.value
                )
                SuggestionTypeEnum.PlaceOfWork -> workExperienceVerificationReadRepository.getSuggestionCompanyAssociatedUser(
                    associatedUserId = BlSecurityUtils.currentUserId,
                    query = q,
                    status = VerificationStatusEnum.Pending.value
                )
                else -> listOf()
            }
        } else if (group == SuggestionGroupEnum.Education) {
            when (type) {
                SuggestionTypeEnum.University -> educationVerificationReadRepository.getSuggestionUniversityAssociatedUser(
                    associatedUserId = BlSecurityUtils.currentUserId,
                    query = q,
                    status = VerificationStatusEnum.Pending.value
                )
                SuggestionTypeEnum.Degree -> educationVerificationReadRepository.getSuggestionDegreeAssociatedUser(
                    associatedUserId = BlSecurityUtils.currentUserId,
                    query = q,
                    status = VerificationStatusEnum.Pending.value
                )
                else -> listOf()
            }
        } else {
            listOf()
        }
    }
}
