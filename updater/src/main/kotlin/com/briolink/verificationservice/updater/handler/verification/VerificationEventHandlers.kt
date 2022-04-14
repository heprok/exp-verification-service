package com.briolink.verificationservice.updater.handler.verification

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandlers
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.verificationservice.common.domain.v1_0.ObjectConfirmType
import com.briolink.verificationservice.common.event.v1_0.VerificationCreatedEvent
import com.briolink.verificationservice.common.event.v1_0.VerificationSyncEvent
import com.briolink.verificationservice.updater.handler.verification.education.EducationVerificationHandlerService
import com.briolink.verificationservice.updater.handler.verification.workexperience.WorkExperienceVerificationHandlerService
import com.briolink.verificationservice.updater.service.SyncService

@EventHandlers(
    EventHandler("VerificationCreatedEvent", "1.0"),
    EventHandler("VerificationUpdatedEvent", "1.0")
)
class VerificationEventHandler(
    private val educationVerificationHandlerService: EducationVerificationHandlerService,
    private val workExperienceVerificationHandlerService: WorkExperienceVerificationHandlerService
) : IEventHandler<VerificationCreatedEvent> {
    override fun handle(event: VerificationCreatedEvent) {
        when (event.data.objectConfirmType) {
            ObjectConfirmType.Education -> educationVerificationHandlerService.createOrUpdate(event.data)
            ObjectConfirmType.WorkExperience -> workExperienceVerificationHandlerService.createOrUpdate(event.data)
        }
    }
}

@EventHandler("VerificationSyncEvent", "1.0")
class UserJobPositionSyncEventHandler(
    private val educationVerificationHandlerService: EducationVerificationHandlerService,
    private val workExperienceVerificationHandlerService: WorkExperienceVerificationHandlerService,
    syncService: SyncService,
) : SyncEventHandler<VerificationSyncEvent>(ObjectSyncEnum.UserJobPosition, syncService) { // TODO: add ObjectSync verification
    override fun handle(event: VerificationSyncEvent) {
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
