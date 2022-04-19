package com.briolink.expverificationservice.api.dataloader

import com.briolink.expverificationservice.api.service.verifcation.education.EducationVerificationService
import com.briolink.expverificationservice.common.dataloader.DataLoader
import com.briolink.expverificationservice.common.domain.v1_0.VerificationStatus
import com.briolink.expverificationservice.common.jpa.read.entity.UserEducationReadEntity
import com.briolink.expverificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.EducationVerificationReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.UniversityReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.UserEducationReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.UserReadRepository
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
@Order(1)
class EducationVerificationDataLoader(
    private val educationVerificationReadRepository: EducationVerificationReadRepository,
    private val universityReadRepository: UniversityReadRepository,
    private var userReadRepository: UserReadRepository,
    private var companyReadRepository: CompanyReadRepository,
    private val userEducationReadRepository: UserEducationReadRepository,
    private val educationVerificationService: EducationVerificationService
) : DataLoader() {

    override fun loadData() {
        if (educationVerificationReadRepository.count().toInt() == 0 &&
            userReadRepository.count().toInt() != 0 &&
            userEducationReadRepository.count().toInt() != 0
        ) {
            val users = userReadRepository.findAll()
            val universities = universityReadRepository.findAll()
            val userEducations = userEducationReadRepository.findAll()

            for (i in 1..COUNT_EDUCATION_VERIFICATION) {
                val randomUser = users.random()
                val randomUniversity = universities.random()

                val notConfirmedEducation = mutableListOf<UserEducationReadEntity>()
                val confirmedEducationWithNotUser = mutableListOf<UserEducationReadEntity>()

                userEducations
                    .filterTo(notConfirmedEducation) {
                        it.userId == randomUser.id && it.status == VerificationStatus.NotConfirmed &&
                            it.universityId == randomUniversity.id
                    }

                userEducations
                    .filterTo(confirmedEducationWithNotUser) {
                        it.userId != randomUser.id && it.status == VerificationStatus.Confirmed &&
                            it.universityId == randomUniversity.id
                    }

                if (notConfirmedEducation.isEmpty() || confirmedEducationWithNotUser.isEmpty()) {
                    continue
                }

                val randomUserEducation = notConfirmedEducation.random()

                val confirmToUsersIds = confirmedEducationWithNotUser
                    .shuffled()
                    .subList(1, Random.nextInt(2, 6))
                    .map { it.userId }

                educationVerificationService.addVerification(
                    userId = randomUser.id, objectId = randomUserEducation.id, userConfirmIds = confirmToUsersIds
                )
            }
        }
    }
}
