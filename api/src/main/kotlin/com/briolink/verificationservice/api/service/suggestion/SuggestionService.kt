package com.briolink.verificationservice.api.service.suggestion

import com.blazebit.persistence.CriteriaBuilderFactory
import com.briolink.lib.location.service.LocationService
import com.briolink.verificationservice.api.service.suggestion.dto.SuggestionGroupEnum
import com.briolink.verificationservice.api.service.suggestion.dto.SuggestionTypeEnum
import com.briolink.verificationservice.api.util.SecurityUtil
import com.briolink.verificationservice.common.jpa.read.entity.PositionReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.ConnectionCompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.ConnectionUserReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.ProjectReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserServiceReadRepository
import com.briolink.verificationservice.common.types.BaseSuggestion
import com.briolink.verificationservice.common.types.ObjectId
import com.briolink.verificationservice.common.types.ObjectTypeEnum
import com.briolink.verificationservice.common.types.Suggestion
import liquibase.pro.packaged.it
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@Component
class SuggestionService(
    private val connectionUserReadRepository: ConnectionUserReadRepository,
    private val connectionCompanyReadRepository: ConnectionCompanyReadRepository,
    private val projectReadRepository: ProjectReadRepository,
    private val userServiceReadRepository: UserServiceReadRepository,
    private val locationService: LocationService,
    private val entityManager: EntityManager,
    private val criteriaBuilderFactory: CriteriaBuilderFactory
) {
    fun getSuggestions(
        group: SuggestionGroupEnum,
        type: SuggestionTypeEnum,
        query: String? = null,
        objectId: ObjectId? = null
    ): List<BaseSuggestion> {
        val q = if (query.isNullOrBlank()) null else query
        return if (
            group == SuggestionGroupEnum.ConnectionWithUser &&
            type == SuggestionTypeEnum.UserFullName &&
            objectId != null
        ) {
            connectionUserReadRepository.getSuggestionFullName(objectId.typeKey, objectId.id, q)
        } else if (
            group == SuggestionGroupEnum.ConnectionWithUser &&
            type == SuggestionTypeEnum.UserCurrentJobPositionTitle &&
            objectId != null
        ) {
            connectionUserReadRepository.getSuggestionPrimaryJobPositionTitle(objectId.typeKey, objectId.id, q)
        } else if (
            group == SuggestionGroupEnum.ConnectionWithUser &&
            type == SuggestionTypeEnum.UserCurrentJobPositionCompanyName &&
            objectId != null
        ) {
            connectionUserReadRepository.getSuggestionPrimaryJobPositionCompanyName(objectId.typeKey, objectId.id, q)
        } else if (
            group == SuggestionGroupEnum.ConnectionWithCompany &&
            type == SuggestionTypeEnum.CompanyName &&
            objectId != null
        ) {
            connectionCompanyReadRepository.getSuggestionName(objectId.typeKey, objectId.id, q)
        } else if (
            group == SuggestionGroupEnum.ConnectionWithCompany &&
            type == SuggestionTypeEnum.CompanyIndustry &&
            objectId != null
        ) {
            connectionCompanyReadRepository.getSuggestionIndustry(objectId.typeKey, objectId.id, q)
        } else if (
            group == SuggestionGroupEnum.ConnectionWithCompany &&
            type == SuggestionTypeEnum.CompanyMarketSegment &&
            objectId != null
        ) {
            connectionCompanyReadRepository.getSuggestionMarketSegment(objectId.typeKey, objectId.id, q)
        } else if (
            type == SuggestionTypeEnum.LocationName
        ) {
            locationService.getSuggestionLocation(q)?.map { Suggestion(it.locationId.toString(), it.name) } ?: listOf()
//            locationServiceOld.getLocations(q)
        } else if (
            group == SuggestionGroupEnum.Project &&
            type == SuggestionTypeEnum.CollaboratorCompanyIndustryName &&
            objectId != null
        ) {
            when (objectId.type) {
                ObjectTypeEnum.User ->
                    projectReadRepository.getSuggestionUserCollaboratorCompanyIndustryName(objectId.id.toString(), q)
                ObjectTypeEnum.Company ->
                    projectReadRepository.getSuggestionCompanyCollaboratorCompanyIndustryName(objectId.id.toString(), q)
                else -> listOf()
            }
        } else if (
            group == SuggestionGroupEnum.Project &&
            type == SuggestionTypeEnum.CollaboratorCompanyName &&
            objectId != null
        ) {
            when (objectId.type) {
                ObjectTypeEnum.User -> projectReadRepository.getSuggestionUserCollaboratorCompanyName(
                    objectId.id.toString(),
                    q
                )
                ObjectTypeEnum.Company -> projectReadRepository.getSuggestionCompanyCollaboratorCompanyName(
                    objectId.toString(),
                    q
                )
                else -> listOf()
            }
        } else if (
            group == SuggestionGroupEnum.Project &&
            type == SuggestionTypeEnum.ServiceName &&
            objectId != null
        ) {
            when (objectId.type) {
                ObjectTypeEnum.User -> projectReadRepository.getSuggestionUserServiceName(objectId.id, q)
                ObjectTypeEnum.Company -> projectReadRepository.getSuggestionCompanyServiceName(objectId.id, q)
                else -> listOf()
            }
        } else if (
            group == SuggestionGroupEnum.Project &&
            type == SuggestionTypeEnum.UserCompanyName &&
            objectId != null &&
            objectId.type == ObjectTypeEnum.User
        ) {
            projectReadRepository.getSuggestionUserCompanyName(objectId.id.toString())
        } else if (
            group == SuggestionGroupEnum.Project &&
            type == SuggestionTypeEnum.Position
        ) {
            val cbf = criteriaBuilderFactory.create(entityManager, PositionReadEntity::class.java)
            val cb = cbf.from(PositionReadEntity::class.java)

            if (!q.isNullOrBlank())
                cb.whereExpression("fts_partial(name, :query) = true").setParameter("query", q)
            if (objectId != null && objectId.type == ObjectTypeEnum.Company) {
                cb.where("userId").eq(SecurityUtil.currentUserId)
                cb.where("companyId").eq(objectId.id)
            } else {
                cb.where("userId").isNull
            }

            cb.maxResults = 10

            return cb.resultList.map { Suggestion(id = it.id.toString(), name = it.name) }
        } else if (
            group == SuggestionGroupEnum.Project &&
            type == SuggestionTypeEnum.CollaboratorRoleName &&
            objectId != null
        ) {
            when (objectId.type) {
                ObjectTypeEnum.User -> projectReadRepository.getSuggestionUserCollaboratorRoleName(
                    objectId.id.toString(),
                    q
                )
                ObjectTypeEnum.Company -> projectReadRepository.getSuggestionCompanyCollaboratorRoleName(
                    objectId.id.toString(),
                    q
                )
                else -> listOf()
            }
        } else if (
            group == SuggestionGroupEnum.UserService &&
            type == SuggestionTypeEnum.ProviderCompanyName &&
            objectId != null
        ) {
            when (objectId.type) {
                ObjectTypeEnum.User -> userServiceReadRepository.getSuggestionProviderCompany(
                    objectId.id, q, hidden = if (SecurityUtil.currentUserId != objectId.id) false else null
                )
                else -> listOf()
            }
        } else if (
            group == SuggestionGroupEnum.UserService &&
            type == SuggestionTypeEnum.ServiceName &&
            objectId != null
        ) {
            when (objectId.type) {
                ObjectTypeEnum.User -> userServiceReadRepository.getSuggestionServiceName(
                    objectId.id, q, hidden = if (SecurityUtil.currentUserId != objectId.id) false else null
                )
                else -> listOf()
            }
        } else {
            listOf()
        }
    }
}
