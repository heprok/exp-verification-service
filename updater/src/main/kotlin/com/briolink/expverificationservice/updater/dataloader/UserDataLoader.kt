package com.briolink.expverificationservice.updater.dataloader

import com.briolink.expverificationservice.common.dataloader.DataLoader
import com.briolink.expverificationservice.common.jpa.read.repository.UserReadRepository
import com.briolink.expverificationservice.updater.handler.user.UserEventData
import com.briolink.expverificationservice.updater.handler.user.UserHandlerService
import java.net.URL
import java.util.UUID
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
@Order(1)
class UserDataLoader(
    private var userReadRepository: UserReadRepository,
    private var userHandlerService: UserHandlerService

) : DataLoader() {
    val listFirstName: List<String> = listOf(
        "Lynch", "Kennedy", "Williams", "Evans", "Jones", "Burton", "Miller", "Smith", "Nelson", "Lucas",
    )

    val listLastName: List<String> = listOf(
        "Scott", "Cynthia", "Thomas", "Thomas", "Lucy", "Dawn", "Jeffrey", "Ann", "Joan", "Lauren",
    )

    override fun loadData() {
        if (userReadRepository.count().toInt() == 0
        ) {
            userHandlerService.createOrUpdate(
                UserEventData(
                    id = UUID.fromString("a7bfe294-2586-452b-b5fc-77700df058d3"),
                    slug = UUID.randomUUID().toString(),
                    firstName = listFirstName.random(),
                    lastName = listLastName.random(),
                    image = if (Random.nextBoolean()) URL("https://placeimg.com/148/148/people") else null,
                )
            )
            for (i in 1..COUNT_USER) {
                userHandlerService.createOrUpdate(
                    UserEventData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        firstName = listFirstName.random(),
                        lastName = listLastName.random(),
                        image = if (Random.nextBoolean()) URL("https://placeimg.com/148/148/people") else null,
                    )
                )
            }
        }
    }

    companion object {
        const val COUNT_USER = 5
    }
}
