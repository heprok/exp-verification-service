package com.briolink.expverificationservice.api.service.verifcation

import com.blazebit.persistence.CriteriaBuilder
import com.blazebit.persistence.PagedList
import com.blazebit.persistence.ParameterHolder
import com.blazebit.persistence.WhereBuilder
import com.briolink.expverificationservice.api.service.verifcation.dto.VerificationCountItem

interface VerificationListRequest {
    var offset: Int
    var limit: Int
}

abstract class VerificationListService<A : VerificationListRequest, B> {

    abstract fun getList(request: A): PagedList<B>
    abstract fun createCB(): CriteriaBuilder<B>
    abstract fun <T> applyFilters(request: A, cb: T): T where T : WhereBuilder<T>, T : ParameterHolder<T>

    abstract fun getTab(request: A, withCount: Boolean): VerificationCountItem
}
