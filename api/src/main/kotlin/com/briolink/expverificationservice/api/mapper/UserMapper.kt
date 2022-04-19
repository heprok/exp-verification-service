package com.briolink.expverificationservice.api.mapper

import com.briolink.expverificationservice.api.types.Image
import com.briolink.expverificationservice.api.types.User
import com.briolink.expverificationservice.common.jpa.read.entity.UserReadEntity

fun User.Companion.fromUserData(data: UserReadEntity.UserData) = User(
    id = data.id.toString(),
    slug = data.slug,
    firstName = data.firstName,
    lastName = data.lastName,
    image = data.image?.let { Image(it) }
)
