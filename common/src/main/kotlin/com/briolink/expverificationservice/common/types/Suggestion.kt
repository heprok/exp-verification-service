package com.briolink.expverificationservice.common.types

interface BaseSuggestion {
    val id: String?
    val name: String
}

data class Suggestion(override val id: String?, override val name: String) : BaseSuggestion
