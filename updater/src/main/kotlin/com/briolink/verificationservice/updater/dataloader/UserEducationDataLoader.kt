package com.briolink.verificationservice.updater.dataloader

import com.briolink.verificationservice.common.dataloader.DataLoader
import com.briolink.verificationservice.common.jpa.read.repository.UniversityReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserEducationReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserReadRepository
import com.briolink.verificationservice.updater.handler.usereducation.UserEducationEventData
import com.briolink.verificationservice.updater.handler.usereducation.UserEducationHandlerService
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.random.Random

@Component
@Order(2)
class UserEducationDataLoader(
    private var universityReadRepository: UniversityReadRepository,
    private val userReadRepository: UserReadRepository,
    private var userEducationReadRepository: UserEducationReadRepository,
    private var userEducationHandlerService: UserEducationHandlerService,

) : DataLoader() {
    val degreeList = listOf(
        "Doctor of Philosophy (Ph.D.)",
        "Master's Degree",
        "Master's Degree, International Politic.",
        "Student",
        "Programmist-techick",
        "Intern"
    )

    override fun loadData() {
        if (universityReadRepository.count().toInt() != 0 &&
            userReadRepository.count().toInt() != 0 &&
            userEducationReadRepository.count().toInt() == 0
        ) {
            val universities = universityReadRepository.findAll()
            val users = userReadRepository.findAll()
            for (i in 1..COUNT_USER_EDUCATION) {
                userEducationHandlerService.createOrUpdate(
                    UserEducationEventData(
                        id = UUID.randomUUID(),
                        universityId = universities.random().id,
                        startDate = randomDate(2010, 2016),
                        endDate = if (Random.nextBoolean()) randomDate(2010, 2016) else null,
                        degree = degreeList.random(),
                        userId = users.random().id
                    )
                )
            }
        }
    }

    companion object {
        const val COUNT_USER_EDUCATION = 10
    }
}
