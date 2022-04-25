package com.briolink.expverificationservice.common.jpa.read.repository

import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.jpa.read.entity.verification.EducationVerificationReadEntity
import com.briolink.expverificationservice.common.types.BaseSuggestion
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.util.UUID

interface EducationVerificationReadRepository : JpaRepository<EducationVerificationReadEntity, UUID> {
    @Modifying
    @Query(
        """update EducationVerificationReadEntity u
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
        """update EducationVerificationReadEntity u
           set
               u.universityName = :name,
               u.universityNameTsv = function('tsv', u.universityName),
               u.userEducationData = function('jsonb_sets', u.userEducationData,
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

    @Modifying
    @Query(
        """update EducationVerificationReadEntity u
           set
               u.degree = :title,
               u.degreeTsv = function('tsv', u.degree),
               u.userEducationData = function('jsonb_sets', u.userEducationData,
                    '{id}', :userEducationId, uuid,
                    '{degree}', :degree, text,
                    '{startDate}', :startDate, date,
                    '{endDate}', :endDate, date
               )
           where u.userEducationId = :userEducationId
        """,
    )
    fun updateEducation(
        @Param("userEducationId") userEducationId: UUID,
        @Param("degree") degree: String,
        @Param("startDate") startDate: LocalDate,
        @Param("endDate") endDate: LocalDate? = null,
    )

    @Query(
        """
            select distinct u.userFullName as name
            from EducationVerificationReadEntity u
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
            select distinct u.degree as name
            from EducationVerificationReadEntity u
            where function('array_contains_element', u.userToConfirmIds, :userId) = true AND
                (:query is null or function('fts_partial_col', u.degreeTsv, :query) = true ) AND
                u._status = :status
        """,
    )
    fun getSuggestionDegreeAssociatedUser(
        @Param("userId") associatedUserId: UUID,
        @Param("query") query: String? = null,
        @Param("status") status: Int = VerificationStatusEnum.Pending.value,
        pageable: Pageable = Pageable.ofSize(10)
    ): List<BaseSuggestion>

    @Query(
        """
            select distinct u.universityId as id, u.universityName as name
            from EducationVerificationReadEntity u
            where function('array_contains_element', u.userToConfirmIds, :userId) = true AND
                (:query is null or function('fts_partial_col', u.universityNameTsv, :query) = true ) AND
                u._status = :status
        """,
    )
    fun getSuggestionUniversityAssociatedUser(
        @Param("userId") associatedUserId: UUID,
        @Param("query") query: String? = null,
        @Param("status") status: Int = VerificationStatusEnum.Pending.value,
        pageable: Pageable = Pageable.ofSize(10)
    ): List<BaseSuggestion>

    @Modifying
    @Query("update EducationVerificationReadEntity u set u._status = ?2 where u.id = ?1")
    fun updateStatusById(id: UUID, status: Int): Int
}
