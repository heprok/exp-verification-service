package com.briolink.verificationservice.api.mapper

import com.briolink.verificationservice.api.types.Company
import com.briolink.verificationservice.api.types.Image
import com.briolink.verificationservice.api.types.User
import com.briolink.verificationservice.api.types.UserJobPosition
import com.briolink.verificationservice.common.jpa.read.entity.UserReadEntity
import com.briolink.verificationservice.common.jpa.read.entity.connection.ConnectionUserReadEntity

fun User.Companion.fromRead(entity: UserReadEntity): User {
    return User(
        id = entity.id.toString(),
        firstName = entity.data.firstName,
        lastName = entity.data.lastName,
        slug = entity.data.slug,
        image = entity.data.image?.let { url -> Image(url) },
        currentJobPosition = entity.data.currentJobPosition?.let { jobPosition ->
            UserJobPosition(
                id = jobPosition.id.toString(), title = jobPosition.title,
                company = Company(
                    id = jobPosition.company.id.toString(),
                    slug = jobPosition.company.slug,
                    name = jobPosition.company.name
                ),
            )
        },
        verifiedConnections = entity.data.verifiedConnections,
        verifiedProjects = entity.data.verifiedProjects
    )
}

fun User.Companion.fromRead(entity: ConnectionUserReadEntity): User {
    return User(
        id = entity.id.targetId.toString(),
        firstName = entity.userData.firstName,
        lastName = entity.userData.lastName,
        slug = entity.userData.slug,
        description = entity.userData.description,
        image = entity.userData.image?.let { image -> Image(image) },
        currentJobPosition = entity.userData.currentJobPosition?.let { currJobPos ->
            UserJobPosition(
                currJobPos.id.toString(), currJobPos.title,
                Company(
                    id = currJobPos.company.id.toString(),
                    name = currJobPos.company.name,
                    slug = currJobPos.company.slug,
                ),
            )
        },
        verifiedConnections = entity.userData.verifiedConnections,
        verifiedProjects = entity.userData.verifiedProjects
    )
}
