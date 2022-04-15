package com.briolink.verificationservice.updater.dataloader

import com.briolink.verificationservice.common.dataloader.DataLoader
import com.briolink.verificationservice.common.jpa.read.repository.UniversityReadRepository
import com.briolink.verificationservice.updater.handler.university.UniversityEventData
import com.briolink.verificationservice.updater.handler.university.UniversityHandlerService
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.net.URL
import java.util.UUID
import kotlin.random.Random

@Component
@Order(1)
class UniversityDataLoader(
    private var universityReadRepository: UniversityReadRepository,
    private var universityHandlerService: UniversityHandlerService

) : DataLoader() {
    val universityNames: List<String> = listOf(
        "Stanford University", "Oxford", "Massachusetts Institute of Technology,", "University of Cambridge",
    )

    override fun loadData() {
        if (universityReadRepository.count().toInt() == 0
        ) {
            universityNames.forEachIndexed { index, name ->
                if (index == COUNT_UNIVERSITY) return@forEachIndexed
                universityHandlerService.createOrUpdate(
                    UniversityEventData(
                        id = UUID.randomUUID(),
                        name = name,
                        logo = if (Random.nextBoolean()) URL("https://placeimg.com/148/148/people") else null,
                    )
                )
            }
        }
    }

    companion object {
        const val COUNT_UNIVERSITY = 5
    }
}
