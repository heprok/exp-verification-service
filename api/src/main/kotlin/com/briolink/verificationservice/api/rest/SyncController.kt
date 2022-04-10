package com.briolink.verificationservice.api.rest

import com.briolink.lib.sync.AbstractSyncController
import com.briolink.lib.sync.model.PeriodDateTime
import com.briolink.verificationservice.api.service.project.ProjectService
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Async
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/v1")
class SyncController(
    private val projectService: ProjectService,
) : AbstractSyncController() {
    @PostMapping("sync")
    @Async
    // @PreAuthorize("@servletUtil.isIntranet()")
    fun postSync(
        @RequestParam startLocalDateTime: String? = null,
        @RequestParam endLocalDateTime: String? = null,
        @NotNull @RequestParam syncId: Int
    ): ResponseEntity<Any> {
        return super.sync(startLocalDateTime, endLocalDateTime, syncId)
    }

    override fun publishSyncEvent(syncId: Int, period: PeriodDateTime?) {
        projectService.publishSyncEvent(syncId, period)
        projectService.syncReadProjectFromWrite(period)
    }
}
