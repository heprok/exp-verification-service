package com.briolink.verificationservice.common.jpa.read.repository

import com.briolink.verificationservice.common.jpa.read.entity.UserJobPositionReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface UserJobPositionReadRepository : JpaRepository<UserJobPositionReadEntity, UUID> {

    @Query("select (count(u) > 0) from UserJobPositionReadEntity u where u.id = ?1 and u.userId = ?2 and u._status = ?3")
    fun existsByIdAndUserIdAndStatus(id: UUID, userId: UUID, status: Int): Boolean

    @Modifying
    @Query(
        """update UserJobPositionReadEntity u
           set 
               u.data = function('jsonb_sets', u.data,
                    '{company,id}', :id, uuid,
                    '{company,slug}', :slug, text,
                    '{company,name}', :firstName, text,
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
}
