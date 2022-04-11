package com.briolink.verificationservice.updater.handler.user

import com.briolink.verificationservice.common.jpa.read.entity.UserReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.UserReadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserHandlerService(
    private val userReadRepository: UserReadRepository
) {
    fun createOrUpdate(domain: UserEventData): UserReadEntity {
        userReadRepository.findById(domain.id).orElse(UserReadEntity(domain.id)).apply {
            data = UserReadEntity.UserData(
                id = domain.id,
                slug = domain.slug,
                firstName = domain.firstName,
                lastName = domain.lastName,
                image = domain.image
            )
            return userReadRepository.save(this)
        }
    }
}