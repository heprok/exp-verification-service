package com.briolink.verificationservice.updater.handler.project

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandlers
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.verificationservice.common.domain.v1_0.ProjectCompanyRoleType
import com.briolink.verificationservice.common.domain.v1_0.ProjectObjectType
import com.briolink.verificationservice.common.domain.v1_0.ProjectStatus
import com.briolink.verificationservice.common.event.v1_0.ProjectCreatedEvent
import com.briolink.verificationservice.common.event.v1_0.ProjectDeletedEvent
import com.briolink.verificationservice.common.event.v1_0.ProjectSyncEvent
import com.briolink.verificationservice.common.event.v1_0.ProjectVisibilityUpdatedEvent
import com.briolink.verificationservice.common.jpa.read.repository.ProjectReadRepository
import com.briolink.verificationservice.common.types.ProjectStageEnum
import com.briolink.verificationservice.updater.handler.companyservice.CompanyServiceHandlerService
import com.briolink.verificationservice.updater.handler.userservice.UserServiceHandlerService
import com.briolink.verificationservice.updater.service.SyncService
import java.time.Year

@EventHandlers(
    EventHandler("ProjectCreatedEvent", "1.0"),
    EventHandler("ProjectUpdatedEvent", "1.0"),
)
class ConnectionEventHandler(
    private val projectReadRepository: ProjectReadRepository,
    private val companyServiceHandlerService: CompanyServiceHandlerService,
    private val userServiceHandlerService: UserServiceHandlerService,
) : IEventHandler<ProjectCreatedEvent> {
    override fun handle(event: ProjectCreatedEvent) {
        val service = event.data.services.firstOrNull()
        val sellerUserId = if (event.data.participantFrom.companyRole?.type == ProjectCompanyRoleType.Seller)
            event.data.participantFrom.userId else event.data.participantTo.userId

        if (event.data.status == ProjectStatus.Verified) {
            if (service?.serviceId != null) {
                val stats =
                    projectReadRepository.getStatsByCompanyService(service.serviceId!!, ProjectStageEnum.Verified.value)
                userServiceHandlerService.createUserService(
                    serviceId = service.serviceId!!,
                    sellerUserId = sellerUserId!!,
                )
                companyServiceHandlerService.updateStats(
                    serviceId = service.serviceId!!,
                    verifiedUsed = stats.toInt(),
                    lastUsedYear = if (service.endDate == null) Year.now() else service.endDate,
                )
            }
        } else if (event.data.status == ProjectStatus.InProgress) {
            if (service?.serviceId != null && !projectReadRepository.existsNotHiddenAndNotDeletedProjectByServiceIdAndUserId(
                    serviceId = service.serviceId!!,
                    sellerUserId = sellerUserId!!,
                )
            ) userServiceHandlerService.deleteUserService(service.serviceId!!, sellerUserId)
        }
    }
}

@EventHandler("ProjectDeletedEvent", "1.0")
class ConnectionDeletedEventHandler(
    private val projectReadRepository: ProjectReadRepository,
    private val userServiceHandlerService: UserServiceHandlerService,
) : IEventHandler<ProjectDeletedEvent> {
    override fun handle(event: ProjectDeletedEvent) {
        if (event.data.serviceId != null && event.data.objectType == ProjectObjectType.User) {
            if (!projectReadRepository.existsNotHiddenAndNotDeletedProjectByServiceIdAndUserId(
                    serviceId = event.data.serviceId!!,
                    sellerUserId = event.data.participantObjectId,
                    status = ProjectStageEnum.Verified.value,
                )
            ) userServiceHandlerService.deleteUserService(event.data.serviceId!!, event.data.participantObjectId)
        }
    }
}

@EventHandler("ProjectVisibilityUpdatedEvent", "1.0")
class ConnectionVisibilityUpdatedEventHandler(
    private val projectReadRepository: ProjectReadRepository,
    private val userServiceHandlerService: UserServiceHandlerService,
) : IEventHandler<ProjectVisibilityUpdatedEvent> {
    override fun handle(event: ProjectVisibilityUpdatedEvent) {
        if (event.data.serviceId != null && event.data.objectType == ProjectObjectType.User) {

//        | existsVerifyProjectByService | hiddenProject | Action
//        | false | true | -> hide userService
//        | false | false | -> open UserService
//        | true | false | -> null
//        | true | true | ->  null

            if (!projectReadRepository.existsNotHiddenAndNotDeletedProjectByServiceIdAndUserId(
                    event.data.serviceId!!,
                    event.data.participantObjectId,
                )
            )
                userServiceHandlerService.toggleVisibilityUserService(
                    event.data.serviceId!!,
                    event.data.participantObjectId,
                    event.data.hidden,
                )
        }
    }
}

@EventHandler("ProjectSyncEvent", "1.0")
class ConnectionSyncEventHandler(
    syncService: SyncService,
) : SyncEventHandler<ProjectSyncEvent>(ObjectSyncEnum.Connection, syncService) {
    override fun handle(event: ProjectSyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        objectSyncCompleted(syncData)
    }
}
