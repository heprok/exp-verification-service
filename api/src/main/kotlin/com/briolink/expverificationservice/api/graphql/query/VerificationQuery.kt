package com.briolink.expverificationservice.api.graphql.query

import com.briolink.expverificationservice.api.mapper.fromEducationVerificationRead
import com.briolink.expverificationservice.api.mapper.fromWorkExpVerificationRead
import com.briolink.expverificationservice.api.service.verifcation.dto.TabVerificationEnum
import com.briolink.expverificationservice.api.service.verifcation.education.EducationVerificationListService
import com.briolink.expverificationservice.api.service.verifcation.education.dto.EducationVerificationListFilter
import com.briolink.expverificationservice.api.service.verifcation.education.dto.EducationVerificationListRequest
import com.briolink.expverificationservice.api.service.verifcation.workexperince.WorkExperienceVerificationListService
import com.briolink.expverificationservice.api.service.verifcation.workexperince.dto.WorkExpVerificationListFilter
import com.briolink.expverificationservice.api.service.verifcation.workexperince.dto.WorkExpVerificationListRequest
import com.briolink.expverificationservice.api.types.Suggestion
import com.briolink.expverificationservice.api.types.SuggestionOptions
import com.briolink.expverificationservice.api.types.Verification
import com.briolink.expverificationservice.api.types.VerificationList
import com.briolink.expverificationservice.api.types.VerificationListOptions
import com.briolink.expverificationservice.api.types.VerificationTabWithCount
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.prepost.PreAuthorize
import java.util.UUID

@DgsComponent
class VerificationQuery(
    private val educationVerificationListService: EducationVerificationListService,
    private val workExperienceVerificationListService: WorkExperienceVerificationListService
) {
    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getSuggestions(
        @InputArgument options: SuggestionOptions
    ): List<Suggestion> {
        return listOf()
    }

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getVerificationList(
        @InputArgument options: VerificationListOptions
    ): VerificationList {
        return when (TabVerificationEnum.fromId(options.tabId)) {
            TabVerificationEnum.WorkExperience -> getWorkExpVerificationList(options)
            TabVerificationEnum.Education -> getEducationVerificationList(options)
        }
    }

    private fun getEducationVerificationList(options: VerificationListOptions): VerificationList {
        val request = EducationVerificationListRequest(
            filters = options.filter?.let {
                EducationVerificationListFilter(
                    universityIds = it.universityIds?.map { UUID.fromString(it) },
                    userFullNames = it.userFullNames,
                    degrees = it.degrees
                )
            },
            offset = options.offset ?: 0,
            limit = options.limit ?: 10
        )
        val items = educationVerificationListService.getList(request)
        return VerificationList(
            items = items.map { Verification.fromEducationVerificationRead(it) },
            itemsCountByTab = listOf(
                workExperienceVerificationListService.getTab(null, true).let {
                    VerificationTabWithCount(id = it.id, name = it.name, value = it.count)
                },
                educationVerificationListService.getTab(request, true).let {
                    VerificationTabWithCount(id = it.id, name = it.name, value = it.count)
                }
            ),
            totalItems = items.totalSize.toInt()
        )
    }

    private fun getWorkExpVerificationList(options: VerificationListOptions): VerificationList {
        val request = WorkExpVerificationListRequest(
            filters = options.filter?.let {
                WorkExpVerificationListFilter(
                    companyIds = it.placeOfWorkCompanyIds?.map { UUID.fromString(it) },
                    userFullNames = it.userFullNames,
                    positionTitles = it.jobTitles
                )
            },
            offset = options.offset ?: 0,
            limit = options.limit ?: 10
        )
        val items = workExperienceVerificationListService.getList(request)
        return VerificationList(
            items = items.map { Verification.fromWorkExpVerificationRead(it) },
            itemsCountByTab = listOf(
                workExperienceVerificationListService.getTab(request, true).let {
                    VerificationTabWithCount(id = it.id, name = it.name, value = it.count)
                },
                educationVerificationListService.getTab(null, true).let {
                    VerificationTabWithCount(id = it.id, name = it.name, value = it.count)
                }
            ),
            totalItems = items.totalSize.toInt()
        )
    }

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getTabs(): List<VerificationTabWithCount> {
        return listOf(
            educationVerificationListService.getTab(null, false).let { VerificationTabWithCount(it.id, it.name, it.count) },
            workExperienceVerificationListService.getTab(null, false).let { VerificationTabWithCount(it.id, it.name, it.count) },
        )
    }
}
