package com.briolink.expverificationservice.updater.handler.company

import com.briolink.lib.event.Event
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL
import java.util.UUID

data class CompanyEventData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val name: String,
    @JsonProperty
    val slug: String,
    @JsonProperty
    val logo: URL? = null,
)

data class CompanyCreatedEvent(override val data: CompanyEventData) : Event<CompanyEventData>("1.0")
data class CompanyUpdatedEvent(override val data: CompanyEventData) : Event<CompanyEventData>("1.0")
data class CompanySyncEvent(override val data: SyncData<CompanyEventData>) : SyncEvent<CompanyEventData>("1.0")
