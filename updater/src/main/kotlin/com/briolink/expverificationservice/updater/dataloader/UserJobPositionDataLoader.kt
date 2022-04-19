package com.briolink.expverificationservice.updater.dataloader

import com.briolink.expverificationservice.common.dataloader.DataLoader
import com.briolink.expverificationservice.common.domain.v1_0.VerificationStatus
import com.briolink.expverificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.UserJobPositionReadRepository
import com.briolink.expverificationservice.common.jpa.read.repository.UserReadRepository
import com.briolink.expverificationservice.updater.handler.userjobposition.UserJobPositionEventData
import com.briolink.expverificationservice.updater.handler.userjobposition.UserJobPositionHandlerService
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.random.Random

@Component
@Order(2)
class UserJobPositionDataLoader(
    private var companyReadRepository: CompanyReadRepository,
    private val userReadRepository: UserReadRepository,
    private var userJobPositionReadRepository: UserJobPositionReadRepository,
    private var userJobPositionHandlerService: UserJobPositionHandlerService,

) : DataLoader() {
    private val jobPositionTitles = listOf(
        "Product Manager",
        "IOS developer",
        "Android developer",
        "UX UI designer",
        "Regional finance manager",
        "Software developer",
        "Web developer",
        "Content maker",
        "Hardware developer",
        "Manager",
    )

    override fun loadData() {
        if (companyReadRepository.count().toInt() != 0 &&
            userReadRepository.count().toInt() != 0 &&
            userJobPositionReadRepository.count().toInt() == 0
        ) {
            val companies = companyReadRepository.findAll()
            val users = userReadRepository.findAll()
            for (i in 1..COUNT_USER_JOB_POSITION) {
                userJobPositionHandlerService.createOrUpdate(
                    UserJobPositionEventData(
                        id = UUID.randomUUID(),
                        companyId = companies.random().id,
                        startDate = randomDate(2010, 2016),
                        endDate = if (Random.nextBoolean()) randomDate(2010, 2016) else null,
                        title = jobPositionTitles.random(),
                        userId = users.random().id
                    ),
                    VerificationStatus.values().random()
                )
            }
        }
    }
}
