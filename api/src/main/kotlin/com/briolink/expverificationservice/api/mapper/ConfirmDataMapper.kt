package com.briolink.expverificationservice.api.mapper

import com.briolink.expverificationservice.api.types.ConfirmData
import com.briolink.expverificationservice.api.types.Image
import com.briolink.expverificationservice.api.types.ObjectInfo
import com.briolink.expverificationservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.expverificationservice.common.jpa.read.entity.UniversityReadEntity
import com.briolink.expverificationservice.common.jpa.read.entity.UserEducationReadEntity
import com.briolink.expverificationservice.common.jpa.read.entity.UserJobPositionReadEntity

fun ObjectInfo.Companion.fromUniversityData(data: UniversityReadEntity.UniversityData) = ObjectInfo(
    id = data.id.toString(),
    slug = null,
    name = data.name,
    image = data.logo?.let { Image(it) }

)

fun ObjectInfo.Companion.fromCompanyData(data: CompanyReadEntity.CompanyData) = ObjectInfo(
    id = data.id.toString(),
    slug = data.slug,
    name = data.name,
    image = data.logo?.let { Image(it) }

)

fun ConfirmData.Companion.fromUserEducationData(data: UserEducationReadEntity.UserEducationData) = ConfirmData(
    objectInfo = ObjectInfo.fromUniversityData(data.university),
    title = data.degree,
    startDate = data.startDate,
    endDate = data.endDate
)

fun ConfirmData.Companion.fromUserJobPositionData(data: UserJobPositionReadEntity.UserJobPositionData) = ConfirmData(
    objectInfo = ObjectInfo.fromCompanyData(data.company),
    title = data.title,
    startDate = data.startDate,
    endDate = data.endDate
)
