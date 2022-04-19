package com.briolink.expverificationservice.api.service.verifcation.education

import com.blazebit.persistence.CriteriaBuilder
import com.blazebit.persistence.CriteriaBuilderFactory
import com.blazebit.persistence.ParameterHolder
import com.blazebit.persistence.WhereBuilder
import com.briolink.expverificationservice.api.service.verifcation.VerificationListService
import com.briolink.expverificationservice.api.service.verifcation.dto.TabVerificationEnum
import com.briolink.expverificationservice.api.service.verifcation.education.dto.EducationVerificationListRequest
import com.briolink.expverificationservice.common.jpa.read.entity.verification.EducationVerificationReadEntity
import org.springframework.stereotype.Service
import javax.persistence.EntityManager

@Service
class EducationVerificationListService(
    private val entityManager: EntityManager,
    private val criteriaBuilderFactory: CriteriaBuilderFactory

) : VerificationListService<EducationVerificationListRequest, EducationVerificationReadEntity>() {
    override val tab: TabVerificationEnum = TabVerificationEnum.Education

    override fun <T> applyFilters(request: EducationVerificationListRequest, cb: T): T where T : WhereBuilder<T>, T : ParameterHolder<T> {
        if (request.filters == null) return cb

        with(request.filters!!) {
            if (!userFullNames.isNullOrEmpty())
                cb.where("userFullName").`in`(userFullNames)
            if (!degrees.isNullOrEmpty())
                cb.where("degree").`in`(degrees)
            if (!universityIds.isNullOrEmpty())
                cb.where("universityId").`in`(universityIds)
        }

        return cb
    }

    override fun createCB(): CriteriaBuilder<EducationVerificationReadEntity> {
        val cbf = criteriaBuilderFactory.create(entityManager, EducationVerificationReadEntity::class.java)
        return cbf.from(EducationVerificationReadEntity::class.java)
    }
}
