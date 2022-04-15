package com.briolink.verificationservice.common.jpa.read.repository

import com.briolink.verificationservice.common.jpa.read.entity.verification.WorkExperienceVerificationReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.util.UUID

interface WorkExperienceVerificationReadRepository : JpaRepository<WorkExperienceVerificationReadEntity, UUID> {
    @Modifying
    @Query(
        """update WorkExperienceVerificationReadEntity u
           set
               u.userFullName = :fullName,
               u.userFullNameTsv = function('tsv', u.userFullName),
               u.userData = function('jsonb_sets', u.userData,
                    '{id}', :userId, uuid,
                    '{slug}', :slug, text,
                    '{firstName}', :firstName, text,
                    '{lastName}', :lastName, text,
                    '{image}', :image, text
               )
           where u.userId = :userId
        """,
    )
    fun updateUser(
        @Param("userId") userId: UUID,
        @Param("slug") slug: String,
        @Param("fullName") fullName: String,
        @Param("firstName") firstName: String,
        @Param("lastName") lastName: String,
        @Param("image") image: String? = null,
    )

    @Modifying
    @Query(
        """update WorkExperienceVerificationReadEntity u
           set
               u.companyName = :name,
               u.companyNameTsv = function('tsv', u.companyName),
               u.userJobPositionData = function('jsonb_sets', u.userJobPositionData,
                    '{company,id}', :companyId, uuid,
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

    @Modifying
    @Query(
        """update WorkExperienceVerificationReadEntity u
           set
               u.jobPositionTitle = :title,
               u.jobPositionTitleTsv = function('tsv', u.jobPositionTitle),
               u.userJobPositionData = function('jsonb_sets', u.userJobPositionData,
                    '{id}', :userJobPositionId, uuid,
                    '{title}', :title, text,
                    '{startDate}', :startDate, date,
                    '{endDate}', :endDate, date
               )
           where u.userJobPositionId = :userJobPositionId
        """,
    )
    fun updateJobPosition(
        @Param("userJobPositionId") userJobPositionId: UUID,
        @Param("title") title: String,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate? = null,
    )
}
