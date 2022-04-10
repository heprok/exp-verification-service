package com.briolink.verificationservice.updater.handler.companyservice

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.verificationservice.common.jpa.read.repository.CompanyServiceReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.ProjectReadRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandler("CompanyServiceDeletedEvent", "1.0")
class CompanyServiceDeletedEventHandler(
    private val companyServiceReadRepository: CompanyServiceReadRepository,
    private val projectReadRepository: ProjectReadRepository
) : IEventHandler<CompanyServiceDeletedEvent> {
    override fun handle(event: CompanyServiceDeletedEvent) {
        companyServiceReadRepository.setDeleted(event.data.id, true)
        projectReadRepository.updateServiceSlug(event.data.id, "-1")
    }
}
