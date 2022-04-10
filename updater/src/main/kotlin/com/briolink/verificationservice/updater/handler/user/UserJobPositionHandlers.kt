package com.briolink.verificationservice.updater.handler.user

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.Utils
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.event.annotation.EventHandlers
import com.briolink.lib.sync.SyncEventHandler
import com.briolink.lib.sync.enumeration.ObjectSyncEnum
import com.briolink.verificationservice.common.jpa.read.entity.PositionReadEntity
import com.briolink.verificationservice.common.jpa.read.entity.UserReadEntity
import com.briolink.verificationservice.common.jpa.read.entity.connection.ConnectionUserReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.ConnectionUserReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.PositionReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserReadRepository
import com.briolink.verificationservice.updater.service.SyncService
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandlers(
    EventHandler("UserJobPositionCreatedEvent", "1.0"),
    EventHandler("UserJobPositionUpdatedEvent", "1.0"),
)
class UserJobPositionHandlers(
    private val companyReadRepository: CompanyReadRepository,
    private val userReadRepository: UserReadRepository,
    private val positionReadRepository: PositionReadRepository,
    private val connectionUserReadRepository: ConnectionUserReadRepository
) : IEventHandler<UserJobPositionEvent> {
    override fun handle(event: UserJobPositionEvent) {
        if (event.data.isCurrent) {
            val company = companyReadRepository.getById(event.data.companyId)
            userReadRepository.setCurrentJobPosition(
                event.data.userId,
                event.data.companyId,
                Utils.jacksonMapper.writeValueAsString(
                    UserReadEntity.CurrentJobPosition(
                        id = event.data.id,
                        title = event.data.title,
                        company = UserReadEntity.Company(
                            id = company.id!!,
                            slug = company.data.slug,
                            name = company.name,
                            industryId = company.data.industryId,
                            industryName = company.data.industryName,
                            marketSegmentId = company.data.marketSegmentId,
                            marketSegmentName = company.data.marketSegmentName,
                        ),
                    ),
                ),
            )
            connectionUserReadRepository.updateUserCurrentJobPosition(
                event.data.userId,
                event.data.companyId,
                company.name,
                event.data.title,
                Utils.jacksonMapper.writeValueAsString(
                    ConnectionUserReadEntity.CurrentJobPosition(
                        id = event.data.id,
                        title = event.data.title,
                        company = ConnectionUserReadEntity.Company(
                            id = company.id!!,
                            slug = company.data.slug,
                            name = company.name,
                        ),
                    ),
                ),
                company.data.marketSegmentId,
                company.data.marketSegmentName,
                company.data.industryId,
                company.data.industryName,
            )
        }

        positionReadRepository.save(
            PositionReadEntity().apply {
                id = event.data.id
                name = event.data.title
                userId = event.data.userId
                jobPositionId = event.data.id
                companyId = event.data.companyId
            },
        )
    }
}

@Transactional
@EventHandler("UserJobPositionDeletedEvent", "1.0")
class UserJobPositionDeletedHandler(
    private val userReadRepository: UserReadRepository,
    private val positionReadRepository: PositionReadRepository
) : IEventHandler<UserJobPositionDeletedEvent> {
    override fun handle(event: UserJobPositionDeletedEvent) {
        if (event.data.isCurrent) userReadRepository.removeCurrentJobPosition(event.data.userId)
        positionReadRepository.deleteByJobPositionId(event.data.id)
    }
}

@Transactional
@EventHandler("UserJobPositionSyncEvent", "1.0")
class UserJobPositionSyncEventHandler(
    private val companyReadRepository: CompanyReadRepository,
    private val userReadRepository: UserReadRepository,
    private val positionReadRepository: PositionReadRepository,
    private val connectionUserReadRepository: ConnectionUserReadRepository,
    syncService: SyncService
) : SyncEventHandler<UserJobPositionSyncEvent>(ObjectSyncEnum.UserJobPosition, syncService) {
    override fun handle(event: UserJobPositionSyncEvent) {
        val syncData = event.data
        if (!objectSyncStarted(syncData)) return
        try {
            val objectSync = syncData.objectSync!!
            if (objectSync.isCurrent) {
                val company = companyReadRepository.getById(objectSync.companyId)
                userReadRepository.setCurrentJobPosition(
                    objectSync.userId,
                    objectSync.companyId,
                    Utils.jacksonMapper.writeValueAsString(
                        UserReadEntity.CurrentJobPosition(
                            id = objectSync.id,
                            title = objectSync.title,
                            company = UserReadEntity.Company(
                                id = company.id!!,
                                slug = company.data.slug,
                                name = company.name,
                                industryId = company.data.industryId,
                                industryName = company.data.industryName,
                                marketSegmentId = company.data.marketSegmentId,
                                marketSegmentName = company.data.marketSegmentName,
                            ),
                        ),
                    ),
                )
                connectionUserReadRepository.updateUserCurrentJobPosition(
                    objectSync.userId,
                    objectSync.companyId,
                    company.name,
                    objectSync.title,
                    Utils.jacksonMapper.writeValueAsString(
                        ConnectionUserReadEntity.CurrentJobPosition(
                            id = objectSync.id,
                            title = objectSync.title,
                            company = ConnectionUserReadEntity.Company(
                                id = company.id!!,
                                slug = company.data.slug,
                                name = company.name,
                            ),
                        ),
                    ),
                    company.data.marketSegmentId,
                    company.data.marketSegmentName,
                    company.data.industryId,
                    company.data.industryName,
                )
            }

            positionReadRepository.save(
                PositionReadEntity().apply {
                    id = objectSync.id
                    name = objectSync.title
                    userId = objectSync.userId
                    jobPositionId = objectSync.id
                    companyId = objectSync.companyId
                },
            )
        } catch (ex: Exception) {
            sendError(syncData, ex)
        }
        objectSyncCompleted(syncData)
    }
}
