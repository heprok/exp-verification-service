package com.briolink.verificationservice.api.service.verifcation

import com.briolink.lib.event.publisher.EventPublisher
import com.briolink.verificationservice.common.enumeration.ObjectConfirmTypeEnum
import com.briolink.verificationservice.common.event.v1_0.VerificationCreatedEvent
import com.briolink.verificationservice.common.event.v1_0.VerificationUpdatedEvent
import com.briolink.verificationservice.common.jpa.write.entity.ObjectConfirmTypeWriteEntity
import com.briolink.verificationservice.common.jpa.write.entity.VerificationWriteEntity
import com.briolink.verificationservice.common.jpa.write.repository.VerificationWriteRepository
import com.briolink.verificationservice.common.mapper.toDomain
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException
import java.util.UUID
import javax.persistence.EntityManager

abstract class VerificationService() {
    protected abstract val entityManager: EntityManager
    protected abstract val eventPublisher: EventPublisher
    protected abstract val verificationWriteRepository: VerificationWriteRepository
    abstract val objectTypeVerification: ObjectConfirmTypeEnum

    /**
     * Get object confirm type write entity reference
     *
     * @return object confirm type write entity
     */
    fun confirmTypeReference(): ObjectConfirmTypeWriteEntity =
        entityManager.getReference(ObjectConfirmTypeWriteEntity::class.java, objectTypeVerification.value)

    fun publishCreatedEvent(entity: VerificationWriteEntity) {
        eventPublisher.publishAsync(VerificationCreatedEvent(entity.toDomain()))
    }

    fun publishUpdatedEvent(entity: VerificationWriteEntity) {
        eventPublisher.publishAsync(VerificationUpdatedEvent(entity.toDomain()))
    }

    fun getById(id: UUID): VerificationWriteEntity =
        verificationWriteRepository.findById(id).orElseThrow { throw DgsEntityNotFoundException("Verification $id not found") }
}
