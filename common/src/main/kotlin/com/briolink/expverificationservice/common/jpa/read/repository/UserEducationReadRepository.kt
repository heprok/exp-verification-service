package com.briolink.expverificationservice.common.jpa.read.repository

import com.briolink.expverificationservice.common.jpa.read.entity.UserEducationReadEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserEducationReadRepository : JpaRepository<UserEducationReadEntity, UUID> {
    @Modifying
    @Query(
        """update UserEducationReadEntity u
           set
               u.data = function('jsonb_sets', u.data,
                    '{university,id}', :universityId, uuid,
                    '{university,logo}', :logo, text,
                    '{university,name}', :name, text
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
