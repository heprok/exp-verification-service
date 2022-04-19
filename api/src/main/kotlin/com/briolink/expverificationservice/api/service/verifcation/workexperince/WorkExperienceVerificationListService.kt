package com.briolink.expverificationservice.api.service.verifcation.workexperince

import com.blazebit.persistence.CriteriaBuilder
import com.blazebit.persistence.CriteriaBuilderFactory
import com.blazebit.persistence.ParameterHolder
import com.blazebit.persistence.WhereBuilder
import com.briolink.expverificationservice.api.service.verifcation.VerificationListService
import com.briolink.expverificationservice.api.service.verifcation.dto.TabVerificationEnum
import com.briolink.expverificationservice.api.service.verifcation.workexperince.dto.WorkExpVerificationListRequest
import com.briolink.expverificationservice.common.jpa.read.entity.verification.WorkExperienceVerificationReadEntity
import org.springframework.stereotype.Service
import javax.persistence.EntityManager

@Service
class WorkExperienceVerificationListService(
    private val entityManager: EntityManager,
    private val criteriaBuilderFactory: CriteriaBuilderFactory

) : VerificationListService<WorkExpVerificationListRequest, WorkExperienceVerificationReadEntity>() {
    override val tab: TabVerificationEnum = TabVerificationEnum.WorkExperience

    override fun <T> applyFilters(request: WorkExpVerificationListRequest, cb: T): T where T : WhereBuilder<T>, T : ParameterHolder<T> {
        if (request.filters == null) return cb

        with(request.filters!!) {
            if (!userFullNames.isNullOrEmpty())
                cb.where("userFullName").`in`(userFullNames)
            if (!positionTitles.isNullOrEmpty())
                cb.where("degree").`in`(positionTitles)
            if (!companyIds.isNullOrEmpty())
                cb.where("universityId").`in`(companyIds)
        }

        return cb
    }

    override fun createCB(): CriteriaBuilder<WorkExperienceVerificationReadEntity> {
        val cbf = criteriaBuilderFactory.create(entityManager, WorkExperienceVerificationReadEntity::class.java)
        return cbf.from(WorkExperienceVerificationReadEntity::class.java)
    }
}
