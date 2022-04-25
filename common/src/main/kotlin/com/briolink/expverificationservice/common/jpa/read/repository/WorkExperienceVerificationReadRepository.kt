package com.briolink.expverificationservice.common.jpa.read.repository

import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.entity.verification.WorkExperienceVerificationReadEntity
import com.briolink.expverificationservice.common.types.BaseSuggestion
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
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
                    '{startDate}', :startDate, text,
                    '{endDate}', :endDate, text
               )
           where u.userJobPositionId = :userJobPositionId
        """,
    )
    fun updateJobPosition(
        @Param("userJobPositionId") userJobPositionId: UUID,
        @Param("title") title: String,
        @Param("startDate") startDate: String,
        @Param("endDate") endDate: String? = null,
    )

    @Query(
        """
            select distinct u.userFullName as name
            from WorkExperienceVerificationReadEntity u
            where function('array_contains_element', u.userToConfirmIds, :userId) = true AND
                (:query is null or function('fts_partial_col', u.userFullNameTsv, :query) = true ) AND
                u._status = :status
        """,
    )
    fun getSuggestionUserFullNameAssociatedUser(
        @Param("userId") associatedUserId: UUID,
        @Param("query") query: String? = null,
        @Param("status") status: Int = VerificationStatusEnum.Pending.value,
        pageable: Pageable = Pageable.ofSize(10)
    ): List<BaseSuggestion>

    @Query(
        """
            select distinct u.jobPositionTitle as name
            from WorkExperienceVerificationReadEntity u
            where function('array_contains_element', u.userToConfirmIds, :userId) = true AND
                (:query is null or function('fts_partial_col', u.jobPositionTitleTsv, :query) = true ) AND
                u._status = :status
        """,
    )
    fun getSuggestionJobPositionAssociatedUser(
        @Param("userId") associatedUserId: UUID,
        @Param("query") query: String? = null,
        @Param("status") status: Int = VerificationStatusEnum.Pending.value,
        pageable: Pageable = Pageable.ofSize(10)
    ): List<BaseSuggestion>

    @Query(
        """
            select distinct u.companyId as id, u.companyName as name
            from WorkExperienceVerificationReadEntity u
            where function('array_contains_element', u.userToConfirmIds, :userId) = true AND
                (:query is null or function('fts_partial_col', u.companyNameTsv, :query) = true ) AND
                u._status = :status
        """,
    )
    fun getSuggestionCompanyAssociatedUser(
        @Param("userId") associatedUserId: UUID,
        @Param("query") query: String? = null,
        @Param("status") status: Int = VerificationStatusEnum.Pending.value,
        pageable: Pageable = Pageable.ofSize(10)
    ): List<BaseSuggestion>

    @Modifying
    @Query("update WorkExperienceVerificationReadEntity u set u._status = ?2 where u.id = ?1")
    fun updateStatusById(id: UUID, status: Int): Int
}
