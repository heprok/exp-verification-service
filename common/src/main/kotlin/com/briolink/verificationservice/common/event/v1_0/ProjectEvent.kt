package com.briolink.verificationservice.common.event.v1_0

import com.briolink.lib.event.Event
import com.briolink.lib.sync.SyncData
import com.briolink.lib.sync.SyncEvent
import com.briolink.verificationservice.common.domain.v1_0.CompanyRole
import com.briolink.verificationservice.common.domain.v1_0.Project
import com.briolink.verificationservice.common.domain.v1_0.ProjectDeletedData
import com.briolink.verificationservice.common.domain.v1_0.ProjectVisibilityData

data class ProjectCreatedEvent(override val data: Project) : Event<Project>("1.0")
data class ProjectSyncEvent(override val data: SyncData<Project>) : SyncEvent<Project>("1.0")
data class ProjectUpdatedEvent(override val data: Project) : Event<Project>("1.0")
data class ProjectDeletedEvent(override val data: ProjectDeletedData) : Event<ProjectDeletedData>("1.0")
data class ProjectVisibilityUpdatedEvent(override val data: ProjectVisibilityData) : Event<ProjectVisibilityData>("1.0")

data class CompanyRoleCreatedEvent(override val data: CompanyRole) : Event<CompanyRole>("1.0")
data class CompanyRoleSyncEvent(override val data: SyncData<CompanyRole>) : SyncEvent<CompanyRole>("1.0")
