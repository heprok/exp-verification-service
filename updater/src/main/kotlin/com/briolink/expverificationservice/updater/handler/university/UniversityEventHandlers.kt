package com.briolink.expverificationservice.updater.handler.university

import com.briolink.expverificationservice.updater.handler.usereducation.UserEducationHandlerService
import com.briolink.expverificationservice.updater.handler.verification.education.EducationVerificationHandlerService
import com.briolink.expverificationservice.updater.service.SyncService
import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandlers
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandlers(
    EventHandler("UniversityCreatedEvent", "1.0"),
    EventHandler("UniversityUpdatedEvent", "1.0")
)
class UniversityEventHandler(
    private val universityHandlerService: UniversityHandlerService,
    private val userEducationHandlerService: UserEducationHandlerService,
    private val educationVerificationHandlerService: EducationVerificationHandlerService,
) : IEventHandler<UniversityCreatedEvent> {
    override fun handle(event: UniversityCreatedEvent) {
        universityHandlerService.createOrUpdate(event.data).also {
            if (event.name == "UniversityUpdatedEvent") {
                userEducationHandlerService.updateUniversity(it)
                educationVerificationHandlerService.updateUniversity(it)
            }
        }
    }
}

@Transactional
@EventHandler("UniversitySyncEvent", "1.0")
class UniversitySyncEventHandler(
    private val universityHandlerService: UniversityHandlerService,
    private val userEducationHandlerService: UserEducationHandlerService,
    private val educationVerificationHandlerService: EducationVerificationHandlerService,
    syncService: SyncService,
) : SyncEventHandler<UniversitySyncEvent>(ObjectSyncEnum.University, syncService) {
    override fun handle(event: UniversitySyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            universityHandlerService.createOrUpdate(objectSync).also {
                userEducationHandlerService.updateUniversity(it)
                educationVerificationHandlerService.updateUniversity(it)
            }
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
