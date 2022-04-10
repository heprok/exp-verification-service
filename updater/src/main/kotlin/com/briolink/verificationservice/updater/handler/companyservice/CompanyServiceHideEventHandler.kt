package com.briolink.verificationservice.updater.handler.companyservice

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.verificationservice.common.jpa.read.repository.CompanyServiceReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.ProjectReadRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandler("CompanyServiceHideEvent", "1.0")
class CompanyServiceHideEventHandler(
    private val companyServiceReadRepository: CompanyServiceReadRepository,
    private val projectReadRepository: ProjectReadRepository
) : IEventHandler<CompanyServiceHideEvent> {
    override fun handle(event: CompanyServiceHideEvent) {
        companyServiceReadRepository.setHidden(event.data.id, event.data.hidden)

        if (event.data.hidden) {
            projectReadRepository.updateServiceSlug(event.data.id, "-1")
        } else {
            projectReadRepository.updateServiceSlug(event.data.id, event.data.slug)
        }
    }
}
