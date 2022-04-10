package com.briolink.verificationservice.updater.handler.company

import com.briolink.lib.event.Event
import com.briolink.lib.location.model.LocationId
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
    val description: String?,
    @JsonProperty
    val logo: URL? = null,
    @JsonProperty
    val industry: IndustryData? = null,
    @JsonProperty
    val website: URL? = null,
    @JsonProperty
    val occupation: OccupationData? = null,
    @JsonProperty
    val locationId: LocationId? = null,
)

data class IndustryData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val name: String,
)

data class OccupationData(
    @JsonProperty
    val id: UUID,
    @JsonProperty
    val name: String
)

data class CompanyCreatedEvent(override val data: CompanyEventData) : Event<CompanyEventData>("1.0")
data class CompanyUpdatedEvent(override val data: CompanyEventData) : Event<CompanyEventData>("1.0")
data class CompanySyncEvent(override val data: SyncData<CompanyEventData>) : SyncEvent<CompanyEventData>("1.0")
