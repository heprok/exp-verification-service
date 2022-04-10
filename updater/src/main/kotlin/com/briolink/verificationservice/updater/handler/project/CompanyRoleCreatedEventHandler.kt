package com.briolink.verificationservice.updater.handler.project

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.verificationservice.common.event.v1_0.CompanyRoleCreatedEvent
import com.briolink.verificationservice.common.jpa.read.entity.CompanyRoleReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyRoleReadRepository
import com.briolink.verificationservice.common.types.CompanyRoleTypeEnum
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandler("CompanyRoleCreatedEvent", "1.0")
class CompanyRoleCreatedEventHandler(
    private val companyRoleReadRepository: CompanyRoleReadRepository,
) : IEventHandler<CompanyRoleCreatedEvent> {
    override fun handle(event: CompanyRoleCreatedEvent) {
        companyRoleReadRepository.save(
            CompanyRoleReadEntity().apply {
                id = event.data.id
                name = event.data.name
                type = CompanyRoleTypeEnum.valueOf(event.data.type.name)
            }
        )
    }
}
