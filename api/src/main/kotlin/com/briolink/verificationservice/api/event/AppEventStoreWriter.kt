package com.briolink.verificationservice.api.event

import com.briolink.lib.event.publisher.EventStoreWriter
import com.briolink.verificationservice.common.jpa.write.entity.EventStoreWriteEntity
import com.briolink.verificationservice.common.jpa.write.repository.EventStoreWriteRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event
import java.time.Instant

@Component
class AppEventStoreWriter(
    val eventStoreWriteRepository: EventStoreWriteRepository
) : EventStoreWriter {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun write(payload: String, timestamp: Long) {
        eventStoreWriteRepository.save(EventStoreWriteEntity(payload, Instant.ofEpochMilli(timestamp)))
    }
}
