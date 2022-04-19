package com.briolink.verificationservice.api.dataloader

import com.briolink.verificationservice.api.service.verifcation.education.EducationVerificationService
import com.briolink.verificationservice.common.dataloader.DataLoader
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.EducationVerificationReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UniversityReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserEducationReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserReadRepository
import com.briolink.verificationservice.common.jpa.write.repository.VerificationWriteRepository
import org.springframework.core.annotation.Order
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
@Order(1)
class EducationVerificationDataLoader(
    private val verificationWriteRepository: VerificationWriteRepository,
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
            val companies = companyReadRepository.findAll()
            val universities = universityReadRepository.findAll()
            val userEducations = userEducationReadRepository.findAll()

            for (i in 1..COUNT_VERIFICATION) {
                val randomUser = users.random()
                val randomUniversity = universities.random()

                val userEducation =

                val confirmToUsers = userEducations.filter { it.userId != randomUser.id && it.universityId == randomUniversity.id }
                    .shuffled()
                    .subList(1, Random.nextInt(2, 6))


                educationVerificationService.addVerification(
                    userId = randomUser.id, objectId = , userConfirmIds = listOf()

                )
            }
        }
    }

    companion object {
        const val COUNT_VERIFICATION = 5
    }
}
