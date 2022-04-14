package com.briolink.verificationservice.common.jpa.read.repository

import com.briolink.verificationservice.common.jpa.read.entity.UserEducationReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface UserEducationReadRepository : JpaRepository<UserEducationReadEntity, UUID> {
    @Query("select (count(u) > 0) from UserEducationReadEntity u where u.id = ?1 and u.userId = ?2 and u._status = ?3")
    fun existsByIdAndUserIdAndStatus(id: UUID, userId: UUID, status: Int): Boolean

    @Modifying
    @Query(
        """update UserEducationReadEntity u
           set 
               u.data = function('jsonb_sets', u.data,
                    '{university,id}', :id, uuid,
                    '{university,slug}', :slug, text,
                    '{university,name}', :firstName, text
               ) 
           where u.universityId = :universityId
        """,
    )
    fun updateUniversity(
        @Param("universityId") universityId: UUID,
        @Param("name") name: String,
        @Param("logo") logo: String? = null,
    )
}
