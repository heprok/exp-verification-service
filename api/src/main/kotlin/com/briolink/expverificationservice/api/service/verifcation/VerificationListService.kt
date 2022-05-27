package com.briolink.expverificationservice.api.service.verifcation

import com.blazebit.persistence.CriteriaBuilder
import com.blazebit.persistence.PagedList
import com.blazebit.persistence.ParameterHolder
import com.blazebit.persistence.WhereBuilder
import com.briolink.expverificationservice.api.service.verifcation.dto.TabVerificationEnum
import com.briolink.expverificationservice.api.service.verifcation.dto.VerificationCountItem
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.entity.verification.BaseVerificationReadEntity
import com.briolink.lib.common.utils.BlSecurityUtils

interface VerificationListRequest {
    var offset: Int
    var limit: Int
}

abstract class VerificationListService<A : VerificationListRequest, B : BaseVerificationReadEntity> {
    abstract val tab: TabVerificationEnum

    fun getList(request: A): PagedList<B> {
        val cb = createCB()

        applyFilters(request, cb)
        applyAssociatedUser(cb)

        return cb.orderByDesc("created").orderByDesc("id").page(request.offset, request.limit).resultList
    }

    fun <T> applyAssociatedUser(cb: T): T where T : WhereBuilder<T>, T : ParameterHolder<T> {
        return cb
            .where("array_contains_element(userToConfirmIds, :userId)").eq(true)
            .where("_status").eq(VerificationStatusEnum.Pending.value)
            .setParameter("userId", BlSecurityUtils.currentUserId)
    }

    abstract fun createCB(): CriteriaBuilder<B>
    abstract fun <T> applyFilters(request: A, cb: T): T where T : WhereBuilder<T>, T : ParameterHolder<T>

    fun getTab(request: A?, withCount: Boolean): VerificationCountItem {
        val tab = VerificationCountItem(id = tab.id, name = tab.name, count = 0)
        if (withCount) {
            val cb = createCB()
            request?.also { applyFilters(request, cb) }
            applyAssociatedUser(cb)
            tab.count = cb.queryRootCountQuery.resultList.first().toInt()
        }
        return tab
    }
}
