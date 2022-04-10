package com.briolink.verificationservice.updater.handler.companyservice

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandlers
import com.briolink.verificationservice.common.jpa.read.repository.ProjectReadRepository
import com.briolink.verificationservice.updater.handler.userservice.UserServiceHandlerService

@EventHandlers(
    EventHandler("CompanyServiceCreatedEvent", "1.0"),
    EventHandler("CompanyServiceUpdatedEvent", "1.0")
)
class CompanyServiceEventHandler(
    private val companyServiceHandlerService: CompanyServiceHandlerService,
    private val projectReadRepository: ProjectReadRepository,
    private val userServiceHandlerService: UserServiceHandlerService,
) : IEventHandler<CompanyServiceCreatedEvent> {
    override fun handle(event: CompanyServiceCreatedEvent) {
        companyServiceHandlerService.createOrUpdate(event.data).also {
            if (event.name == "CompanyServiceUpdatedEvent") {
                projectReadRepository.updateService(it.id!!, it.name, it.slug)
                userServiceHandlerService.updateCompanyService(it)
            }
        }
    }
}
