package com.briolink.expverificationservice.api.event

import com.briolink.expverificationservice.common.jpa.write.entity.EventStoreWriteEntity
import com.briolink.expverificationservice.common.jpa.write.repository.EventStoreWriteRepository
import com.briolink.lib.event.publisher.EventStoreWriter
import java.time.Instant
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class AppEventStoreWriter(
    val eventStoreWriteRepository: EventStoreWriteRepository
) : EventStoreWriter {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun write(payload: String, timestamp: Long) {
        eventStoreWriteRepository.save(EventStoreWriteEntity(payload, Instant.ofEpochMilli(timestamp)))
    }
}
