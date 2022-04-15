package com.briolink.verificationservice.updater.handler.university

import com.briolink.lib.event.Event
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL
import java.util.UUID

data class UniversityEventData(
    @JsonProperty("id")
    val id: UUID,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("logo")
    val logo: URL? = null
)

data class UniversityCreatedEvent(override val data: UniversityEventData) : Event<UniversityEventData>("1.0")
data class UniversitySyncEvent(override val data: SyncData<UniversityEventData>) : SyncEvent<UniversityEventData>("1.0")
