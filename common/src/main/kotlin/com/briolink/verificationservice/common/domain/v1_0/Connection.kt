package com.briolink.verificationservice.common.domain.v1_0

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import java.time.Instant
import java.util.UUID

enum class ConnectionObjectType(val value: Int) {
    @JsonProperty("1")
    User(1),
    @JsonProperty("2")
    Company(2);

    companion object {
        private val map = values().associateBy(ConnectionObjectType::value)
        fun ofValue(value: Int): ConnectionObjectType = map[value]!!
    }
}

enum class ConnectionStatus(@JsonValue val value: Int) {
    NotConnected(0),
    Pending(1),
    AwaitingResponse(2),
    Connected(3),
    Rejected(4);

    companion object {
        private val map = values().associateBy(ConnectionStatus::value)
        fun ofValue(value: Int): ConnectionStatus = map[value]!!
    }
}

data class Connection(
    @JsonProperty
    val fromObjectId: UUID,
    @JsonProperty
    val fromObjectType: ConnectionObjectType,
    @JsonProperty
    val toObjectId: UUID?,
    @JsonProperty
    val toObjectType: ConnectionObjectType,
    @JsonProperty
    val message: String?,
    @JsonProperty
    val status: ConnectionStatus,
    @JsonProperty
    val hidden: Boolean,
    @JsonProperty
    var created: Instant,
    @JsonProperty
    var changed: Instant?,
    @JsonProperty
    var accepted: Instant?
) : Domain
