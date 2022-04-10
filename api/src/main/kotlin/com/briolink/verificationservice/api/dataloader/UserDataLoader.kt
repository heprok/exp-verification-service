package com.briolink.verificationservice.api.dataloader

import com.briolink.verificationservice.api.service.user.UserService
import com.briolink.verificationservice.common.jpa.read.entity.UserReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserReadRepository
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.net.URL
import java.util.UUID
import kotlin.random.Random

@Component
@Order(2)
class UserDataLoader(
    var userReadRepository: UserReadRepository,
    private val companyReadRepository: CompanyReadRepository,
    private val userService: UserService,

) : DataLoader() {
    val listFirstName: List<String> = listOf(
        "Lynch", "Kennedy", "Williams", "Evans", "Jones", "Burton", "Miller", "Smith", "Nelson", "Lucas",
    )

    val listLastName: List<String> = listOf(
        "Scott", "Cynthia", "Thomas", "Thomas", "Lucy", "Dawn", "Jeffrey", "Ann", "Joan", "Lauren",
    )

    override fun loadData() {
        if (userReadRepository.count().toInt() == 0 &&
            companyReadRepository.count().toInt() != 0
        ) {
            val companies = companyReadRepository.findAll()
            var randomCompany = companies.random()
            userService.createReadUser(
                UserReadEntity().apply {
                    id = UUID.fromString("a7bfe294-2586-452b-b5fc-77700df058d3")
                    name = listFirstName.random()
                    currentJobPositionCompanyId = randomCompany.id
                    personalEmail = UUID.randomUUID().toString()
                    data = UserReadEntity.Data(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        firstName = listFirstName.random(),
                        currentJobPosition = UserReadEntity.CurrentJobPosition(
                            id = UUID.randomUUID(),
                            title = "Job position",
                            company = UserReadEntity.Company(
                                id = randomCompany.id!!,
                                slug = randomCompany.data.slug,
                                name = randomCompany.data.name,
                                industryId = randomCompany.data.industryId,
                                industryName = randomCompany.data.industryName,
                                marketSegmentId = randomCompany.data.marketSegmentId,
                                marketSegmentName = randomCompany.data.marketSegmentName,
                            )
                        ),
                        lastName = listLastName.random(),
                        image = if (Random.nextBoolean()) URL("https://placeimg.com/148/148/people") else null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                },
            )
            for (i in 1..COUNT_USER) {
                randomCompany = companies.random()
                userService.createReadUser(
                    UserReadEntity().apply {
                        id = UUID.randomUUID()
                        name = listFirstName.random()
                        currentJobPositionCompanyId = companies.random().id!!
                        personalEmail = UUID.randomUUID().toString()
                        data = UserReadEntity.Data(
                            id = UUID.randomUUID(),
                            currentJobPosition = UserReadEntity.CurrentJobPosition(
                                id = UUID.randomUUID(),
                                title = "Job position",
                                company = UserReadEntity.Company(
                                    id = randomCompany.id!!,
                                    slug = randomCompany.data.slug,
                                    name = randomCompany.data.name,
                                    industryId = randomCompany.data.industryId,
                                    industryName = randomCompany.data.industryName,
                                    marketSegmentId = randomCompany.data.marketSegmentId,
                                    marketSegmentName = randomCompany.data.marketSegmentName,
                                )
                            ),
                            slug = UUID.randomUUID().toString(),
                            firstName = listFirstName.random(),
                            lastName = listLastName.random(),
                            image = if (Random.nextBoolean()) URL("https://placeimg.com/148/148/people") else null,
                            description = null,
                            location = null,
                            verifiedProjects = 0,
                            verifiedConnections = 0,
                        )
                    },
                )
            }
        }
    }

    companion object {
        const val COUNT_USER = 5
    }
}
