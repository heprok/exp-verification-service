package com.briolink.expverificationservice.api.dataloader

import com.briolink.expverificationservice.api.service.verifcation.workexperince.WorkExperienceVerificationService
import com.briolink.expverificationservice.common.dataloader.DataLoader
import com.briolink.expverificationservice.common.domain.v1_0.VerificationStatus
import com.briolink.expverificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.UserJobPositionReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.UserReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.WorkExperienceVerificationReadRepository
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
@Order(1)
class WorkExperienceVerificationDataLoader(
    private val workExperienceVerificationReadRepository: WorkExperienceVerificationReadRepository,
    private val userReadRepository: UserReadRepository,
    private val userJobPositionRepository: UserJobPositionReadRepository,
    private val companyReadRepository: CompanyReadRepository,
    private val workExperienceVerificationService: WorkExperienceVerificationService
) : DataLoader() {

    override fun loadData() {
        if (workExperienceVerificationReadRepository.count().toInt() == 0 &&
            userReadRepository.count().toInt() != 0 &&
            userJobPositionRepository.count().toInt() != 0
        ) {
            val users = userReadRepository.findAll()
            val companies = companyReadRepository.findAll()
            val userJobPositions = userJobPositionRepository.findAll()

            for (i in 1..COUNT_WORKEXP_VERIFICATION) {
                val randomUser = users.random()
                val randomCompany = companies.random()

                val randomUserJobPosition = userJobPositions
                    .filter {
                        it.userId == randomUser.id && it.status == VerificationStatus.NotConfirmed &&
                            it.companyId == randomCompany.id
                    }
                    .random()
                println("randomUserJobPosition: $randomUserJobPosition")

                val confirmToUsersIds = userJobPositions
                    .filter {
                        it.userId != randomUser.id && it.status == VerificationStatus.Confirmed &&
                            it.companyId == randomCompany.id
                    }
                    .shuffled()
                    .subList(1, Random.nextInt(2, 6))
                    .map { it.userId }

                println("confirmToUsersIds: $confirmToUsersIds")
                workExperienceVerificationService.addVerification(
                    userId = randomUser.id, objectId = randomUserJobPosition.id, userConfirmIds = confirmToUsersIds

                )
            }
        }
    }
}