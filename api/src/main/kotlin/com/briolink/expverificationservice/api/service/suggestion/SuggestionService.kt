package com.briolink.expverificationservice.api.service.suggestion

import com.briolink.expverificationservice.api.service.suggestion.dto.SuggestionGroupEnum
import com.briolink.expverificationservice.api.service.suggestion.dto.SuggestionTypeEnum
import com.briolink.expverificationservice.api.util.SecurityUtil
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.repository.EducationVerificationReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.WorkExperienceVerificationReadRepository
import com.briolink.expverificationservice.common.types.BaseSuggestion
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
    ): List<BaseSuggestion> {
        val q = if (query.isNullOrBlank()) null else query
        return if (type == SuggestionTypeEnum.UserFullName) {
            when (group) {
                SuggestionGroupEnum.Education -> educationVerificationReadRepository.getSuggestionUserFullNameAssociatedUser(
                    associatedUserId = SecurityUtil.currentUserId,
                    query = q,
                    status = VerificationStatusEnum.Pending.value
                )
                SuggestionGroupEnum.WorkExperience -> workExperienceVerificationReadRepository.getSuggestionUserFullNameAssociatedUser(
                    associatedUserId = SecurityUtil.currentUserId,
                    query = q,
                    status = VerificationStatusEnum.Pending.value
                )
            }
        } else if (group == SuggestionGroupEnum.WorkExperience) {
            when (type) {
                SuggestionTypeEnum.JobPosition -> workExperienceVerificationReadRepository.getSuggestionJobPositionAssociatedUser(
                    associatedUserId = SecurityUtil.currentUserId,
                    query = q,
                    status = VerificationStatusEnum.Pending.value
                )
                SuggestionTypeEnum.PlaceOfWork -> workExperienceVerificationReadRepository.getSuggestionCompanyAssociatedUser(
                    associatedUserId = SecurityUtil.currentUserId,
                    query = q,
                    status = VerificationStatusEnum.Pending.value
                )
                else -> listOf()
            }
        } else if (group == SuggestionGroupEnum.Education) {
            when (type) {
                SuggestionTypeEnum.University -> educationVerificationReadRepository.getSuggestionUniversityAssociatedUser(
                    associatedUserId = SecurityUtil.currentUserId,
                    query = q,
                    status = VerificationStatusEnum.Pending.value
                )
                SuggestionTypeEnum.Degree -> educationVerificationReadRepository.getSuggestionDegreeAssociatedUser(
                    associatedUserId = SecurityUtil.currentUserId,
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
