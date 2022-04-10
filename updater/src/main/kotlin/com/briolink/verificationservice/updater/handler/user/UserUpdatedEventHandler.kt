package com.briolink.verificationservice.updater.handler.user

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.lib.location.model.LocationMinInfo
import com.briolink.lib.location.service.LocationService
import com.briolink.verificationservice.common.jpa.read.repository.ConnectionUserReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.ProjectReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserReadRepository
import com.vladmihalcea.hibernate.type.util.ObjectMapperWrapper
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandler("UserUpdatedEvent", "1.0")
class UserUpdatedEventHandler(
    private val projectReadRepository: ProjectReadRepository,
    private val userReadRepository: UserReadRepository,
    private val locationService: LocationService,
    private val connectionUserReadRepository: ConnectionUserReadRepository
) : IEventHandler<UserUpdatedEvent> {
    override fun handle(event: UserUpdatedEvent) {
        val location = event.data.locationId?.let {
            locationService.getLocationInfo(it, LocationMinInfo::class.java)
        }

        userReadRepository.update(
            event.data.id,
            "${event.data.firstName} ${event.data.lastName}".trim(),
            event.data.personalEmail,
            event.data.confirmed,
            event.data.slug,
            event.data.firstName,
            event.data.lastName,
            location?.let { ObjectMapperWrapper.INSTANCE.objectMapper.writeValueAsString(it) } ?: "null",
            event.data.description,
            event.data.image?.toString(),
        )
        projectReadRepository.updateUser(
            userId = event.data.id,
            firstName = event.data.firstName,
            lastName = event.data.lastName,
            slug = event.data.slug,
            image = event.data.image?.toString(),
        )
        connectionUserReadRepository.updateRelationship(
            id = event.data.id,
            name = "${event.data.firstName} ${event.data.lastName}",
            firstName = event.data.firstName,
            lastName = event.data.lastName,
            slug = event.data.slug,
            description = event.data.description,
            countryId = location?.country?.id,
            stateId = location?.state?.id,
            cityId = location?.city?.id,
            image = event.data.image?.toString()
        )
    }
}
