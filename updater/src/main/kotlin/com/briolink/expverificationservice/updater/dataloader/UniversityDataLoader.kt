package com.briolink.expverificationservice.updater.dataloader

import com.briolink.expverificationservice.common.jpa.read.repository.UniversityReadRepository
import com.briolink.expverificationservice.updater.handler.university.UniversityEventData
import com.briolink.expverificationservice.updater.handler.university.UniversityHandlerService
import com.briolink.lib.common.utils.BlDataLoader
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

) : BlDataLoader() {

    companion object {
        const val COUNT_UNIVERSITY = 100
    }

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
}
