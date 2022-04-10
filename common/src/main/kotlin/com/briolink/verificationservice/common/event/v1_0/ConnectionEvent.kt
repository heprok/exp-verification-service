package com.briolink.verificationservice.common.event.v1_0

import com.briolink.lib.event.Event
import com.briolink.verificationservice.common.domain.v1_0.Connection

data class ConnectionCreatedEvent(override val data: Connection) : Event<Connection>("1.0")
data class ConnectionUpdatedEvent(override val data: Connection) : Event<Connection>("1.0")
data class ConnectionAcceptedEvent(override val data: Connection) : Event<Connection>("1.0")
data class ConnectionVisibilityUpdatedEvent(override val data: Connection) : Event<Connection>("1.0")
data class ConnectionDeletedEvent(override val data: Connection) : Event<Connection>("1.0")
