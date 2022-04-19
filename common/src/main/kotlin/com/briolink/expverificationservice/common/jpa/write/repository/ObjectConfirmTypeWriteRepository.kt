package com.briolink.expverificationservice.common.jpa.write.repository

import com.briolink.expverificationservice.common.jpa.write.entity.ObjectConfirmTypeWriteEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ObjectConfirmTypeWriteRepository : JpaRepository<ObjectConfirmTypeWriteEntity, Int>
