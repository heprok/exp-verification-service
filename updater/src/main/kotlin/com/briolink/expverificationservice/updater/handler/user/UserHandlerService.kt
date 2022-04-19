package com.briolink.expverificationservice.updater.handler.user

import com.briolink.expverificationservice.common.jpa.read.entity.UserReadEntity
import com.briolink.expverificationservice.common.jpa.read.repository.UserReadRepository
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

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

    fun getById(id: UUID): UserReadEntity =
        userReadRepository.findById(id).orElseThrow { EntityNotFoundException("User with id $id not found") }

    fun updateUser(entity: UserReadEntity) {
    }
}
