package com.briolink.verificationservice.updater.handler.companyservice

import com.briolink.lib.event.Event
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URL
import java.util.UUID

data class CompanyServiceEventData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val name: String,
    @JsonProperty
    val slug: String,
    @JsonProperty
    val companyId: UUID,
    @JsonProperty
    val description: String? = null,
    @JsonProperty
    val logo: URL? = null,
    @JsonProperty
    val price: Double? = null,
    @JsonProperty
    val hidden: Boolean,
    @JsonProperty
    val deleted: Boolean
)

data class CompanyServiceDeletedData(
    @JsonProperty
    val id: UUID
)

data class CompanyServiceHideData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val hidden: Boolean,
    @JsonProperty
    val slug: String
)

data class CompanyServiceCreatedEvent(override val data: CompanyServiceEventData) : Event<CompanyServiceEventData>("1.0")
data class CompanyServiceUpdatedEvent(override val data: CompanyServiceEventData) : Event<CompanyServiceEventData>("1.0")
data class CompanyServiceDeletedEvent(override val data: CompanyServiceDeletedData) : Event<CompanyServiceDeletedData>("1.0")
data class CompanyServiceHideEvent(override val data: CompanyServiceHideData) : Event<CompanyServiceHideData>("1.0")
data class CompanyServiceSyncEvent(override val data: SyncData<CompanyServiceEventData>) : SyncEvent<CompanyServiceEventData>("1.0")
