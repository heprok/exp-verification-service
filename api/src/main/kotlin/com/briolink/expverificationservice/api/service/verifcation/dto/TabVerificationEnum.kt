package com.briolink.expverificationservice.api.service.verifcation.dto

enum class TabVerificationEnum(val id: String, val title: String) {
    Education(id = "studied_with_you", title = "Studied with you"),
    WorkExperience(id = "worked_with_you", title = "Worked with you");

    companion object {
        fun getByIdOrNull(id: String): TabVerificationEnum? {
            return values().find { it.id == id }
        }
    }
}
