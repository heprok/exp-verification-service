package com.briolink.verificationservice.updater.handler.user

import com.briolink.lib.event.IEventHandler
import com.briolink.lib.event.annotation.EventHandler
import com.briolink.verificationservice.common.jpa.read.repository.UserReadRepository
import org.springframework.transaction.annotation.Transactional

@Transactional
@EventHandler("UserJobPositionDeletedEvent", "1.0")
class UserJobPositionDeletedEventHandler(
    private val userReadRepository: UserReadRepository
) : IEventHandler<UserJobPositionDeletedEvent> {
    override fun handle(event: UserJobPositionDeletedEvent) {
        if (event.data.isCurrent) userReadRepository.removeCurrentJobPosition(event.data.userId)
    }
}
