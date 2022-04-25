package com.briolink.expverificationservice.updater.handler.verification

import com.briolink.expverificationservice.common.domain.v1_0.ObjectConfirmType
import com.briolink.expverificationservice.common.enumeration.VerificationStatusEnum
import com.briolink.expverificationservice.common.event.v1_0.ExpVerificationChangedStatusEvent
import com.briolink.expverificationservice.common.event.v1_0.ExpVerificationCreatedEvent
import com.briolink.expverificationservice.common.event.v1_0.ExpVerificationSyncEvent
import com.briolink.expverificationservice.updater.handler.usereducation.UserEducationHandlerService
import com.briolink.expverificationservice.updater.handler.userjobposition.UserJobPositionHandlerService
import com.briolink.expverificationservice.updater.handler.verification.education.EducationVerificationHandlerService
import com.briolink.expverificationservice.updater.handler.verification.workexperience.WorkExperienceVerificationHandlerService
import com.briolink.expverificationservice.updater.service.SyncService
import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandlers
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum

@EventHandlers(
    EventHandler("ExpVerificationCreatedEvent", "1.0"),
    EventHandler("ExpVerificationUpdatedEvent", "1.0")
)
class ExpVerificationEventHandler(
    private val educationVerificationHandlerService: EducationVerificationHandlerService,
    private val workExperienceVerificationHandlerService: WorkExperienceVerificationHandlerService
) : IEventHandler<ExpVerificationCreatedEvent> {
    override fun handle(event: ExpVerificationCreatedEvent) {
        when (event.data.objectConfirmType) {
            ObjectConfirmType.Education -> {
                educationVerificationHandlerService.createOrUpdate(event.data)
            }
            ObjectConfirmType.WorkExperience -> workExperienceVerificationHandlerService.createOrUpdate(event.data)
        }
    }
}

@EventHandler("ExpVerificationSyncEvent", "1.0")
class ExpVerificationSyncEventHandler(
    private val educationVerificationHandlerService: EducationVerificationHandlerService,
    private val workExperienceVerificationHandlerService: WorkExperienceVerificationHandlerService,
    syncService: SyncService,
) : SyncEventHandler<ExpVerificationSyncEvent>(ObjectSyncEnum.UserJobPosition, syncService) { // TODO: add ObjectSync verification
    override fun handle(event: ExpVerificationSyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            when (objectSync.objectConfirmType) {
                ObjectConfirmType.Education -> educationVerificationHandlerService.createOrUpdate(objectSync)
                ObjectConfirmType.WorkExperience -> workExperienceVerificationHandlerService.createOrUpdate(objectSync)
            }
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}

@EventHandler("ExpVerificationChangedStatusEvent", "1.0")
class ExpVerificationChangeStatusEventHandler(
    private val userEducationHandlerService: UserEducationHandlerService,
    private val userJobPositionHandlerService: UserJobPositionHandlerService,
) : IEventHandler<ExpVerificationChangedStatusEvent> {
    override fun handle(event: ExpVerificationChangedStatusEvent) {
        when (event.data.objectConfirmType) {
            ObjectConfirmType.Education -> userEducationHandlerService.updateStatus(
                event.data.objectConfirmId,
                VerificationStatusEnum.ofValue(event.data.status.value)
            )
            ObjectConfirmType.WorkExperience -> userJobPositionHandlerService.updateStatus(
                event.data.objectConfirmId,
                VerificationStatusEnum.ofValue(event.data.status.value)
            )
        }
    }
}
