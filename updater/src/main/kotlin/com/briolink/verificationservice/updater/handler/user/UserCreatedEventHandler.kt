package com.briolink.verificationservice.updater.handler.user

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.location.model.LocationMinInfo
import com.briolink.lib.location.service.LocationService
import com.briolink.verificationservice.common.jpa.read.entity.UserReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.UserReadRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandler("UserCreatedEvent", "1.0")
class UserCreatedEventHandler(
    private val userReadRepository: UserReadRepository,
    private val locationService: LocationService
) : IEventHandler<UserCreatedEvent> {
    override fun handle(event: UserCreatedEvent) {
        userReadRepository.save(
            UserReadEntity().apply {
                id = event.data.id
                name = "${event.data.firstName} ${event.data.lastName}"
                personalEmail = event.data.personalEmail
                confirmed = event.data.confirmed
                data = UserReadEntity.Data(
                    id = event.data.id,
                    slug = event.data.slug,
                    image = event.data.image,
                    firstName = event.data.firstName,
                    lastName = event.data.lastName,
                    location = event.data.locationId?.let { locationService.getLocationInfo(it, LocationMinInfo::class.java) },
                    description = event.data.description,
                    verifiedProjects = 0,
                    verifiedConnections = 0
                )
            }
        )
    }
}
