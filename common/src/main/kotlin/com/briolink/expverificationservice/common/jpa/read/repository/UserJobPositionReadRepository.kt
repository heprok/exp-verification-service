package com.briolink.expverificationservice.common.jpa.read.repository

import com.briolink.expverificationservice.common.jpa.read.entity.UserJobPositionReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface UserJobPositionReadRepository : JpaRepository<UserJobPositionReadEntity, UUID> {

    @Modifying
    @Query(
        """update UserJobPositionReadEntity u
           set
               u.data = function('jsonb_sets', u.data,
                    '{company,id}', :companyId, uuid,
                    '{company,slug}', :slug, text,
                    '{company,name}', :name, text,
                    '{company,logo}', :logo, text
               )
           where u.companyId = :companyId
        """,
    )
    fun updateCompany(
        @Param("companyId") companyId: UUID,
        @Param("slug") slug: String,
        @Param("name") name: String,
        @Param("logo") logo: String? = null,
    )

    @Query("select (count(u) > 0) from UserJobPositionReadEntity u where u.id = ?1 and u.userId = ?2 and u._status = ?3")
    fun existByIdAndUserAndStatus(id: UUID, userId: UUID, status: Int): Boolean
}
