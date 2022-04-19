package com.briolink.expverificationservice.api.service.verifcation.dto

enum class TabVerificationEnum(val id: String, val title: String) {
    Education(id = "studies_with_you", title = "Studied with you"),
    WorkExperience(id = "worked_with_you", title = "Worked with you");

    companion object {
        private val map = values().associateBy(TabVerificationEnum::id)
        fun fromId(id: String): TabVerificationEnum = map[id]!!
    }
}
