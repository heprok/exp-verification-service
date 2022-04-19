package com.briolink.expverificationservice.api.service.verifcation.education

import com.blazebit.persistence.CriteriaBuilder
import com.blazebit.persistence.CriteriaBuilderFactory
import com.blazebit.persistence.PagedList
import com.blazebit.persistence.ParameterHolder
import com.blazebit.persistence.WhereBuilder
import com.briolink.expverificationservice.api.service.verifcation.VerificationListService
import com.briolink.expverificationservice.api.service.verifcation.dto.VerificationCountItem
import com.briolink.expverificationservice.api.service.verifcation.education.dto.EducationVerificationListRequest
import com.briolink.expverificationservice.common.jpa.read.entity.verification.EducationVerificationReadEntity
import javax.persistence.EntityManager

class EducationVerificationListService(
    private val entityManager: EntityManager,
    private val criteriaBuilderFactory: CriteriaBuilderFactory

) : VerificationListService<EducationVerificationListRequest, EducationVerificationReadEntity>() {

    override fun getList(request: EducationVerificationListRequest): PagedList<EducationVerificationReadEntity> {
        val cb = createCB()

        applyFilters(request, cb)

        return cb.orderByDesc("created").orderByDesc("id").page(request.offset, request.limit).resultList
    }

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

    override fun getTab(request: EducationVerificationListRequest, withCount: Boolean): VerificationCountItem {
        val tab = VerificationCountItem(id = "studied_with_you", name = "Studied with you", count = 0)
        if (withCount) {
            val cb = createCB()
            applyFilters(request, cb)
            tab.count = cb.queryRootCountQuery.resultList.first().toInt()
        }
        return tab
    }

    override fun createCB(): CriteriaBuilder<EducationVerificationReadEntity> {
        val cbf = criteriaBuilderFactory.create(entityManager, EducationVerificationReadEntity::class.java)
        return cbf.from(EducationVerificationReadEntity::class.java)
    }
}
