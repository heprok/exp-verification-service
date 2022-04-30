package com.briolink.expverificationservice.api.rest

import com.briolink.expverificationservice.api.service.verifcation.education.EducationVerificationService
import com.briolink.expverificationservice.api.service.verifcation.workexperince.WorkExperienceVerificationService
import com.briolink.lib.sync.AbstractSyncController
import com.briolink.lib.sync.model.PeriodDateTime
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
    private val educationVerificationService: EducationVerificationService,
    private val workExperienceVerificationService: WorkExperienceVerificationService,
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
        educationVerificationService.publishSyncEvent(syncId, period)
        workExperienceVerificationService.publishSyncEvent(syncId, period)
    }
}
