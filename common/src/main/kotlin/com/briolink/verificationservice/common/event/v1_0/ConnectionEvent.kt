package com.briolink.verificationservice.common.event.v1_0

import com.briolink.lib.event.Event
import com.briolink.verificationservice.common.domain.v1_0.Verification

data class VerificationCreatedEvent(override val data: Verification) : Event<Verification>("1.0")
data class VerificationUpdatedEvent(override val data: Verification) : Event<Verification>("1.0")
