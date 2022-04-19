package com.briolink.expverificationservice.api.graphql.query

import com.briolink.expverificationservice.api.service.suggestion.SuggestionService
import com.briolink.expverificationservice.api.service.suggestion.dto.SuggestionGroupEnum
import com.briolink.expverificationservice.api.service.suggestion.dto.SuggestionTypeEnum
import com.briolink.expverificationservice.api.types.Suggestion
import com.briolink.expverificationservice.api.types.SuggestionOptions
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import org.springframework.security.access.prepost.PreAuthorize

@DgsComponent
class SuggestionQuery(private val suggestionService: SuggestionService) {
    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    fun getSuggestions(
        @InputArgument options: SuggestionOptions
    ): List<Suggestion> {
        return suggestionService.getSuggestions(
            group = SuggestionGroupEnum.valueOf(options.group.name),
            type = SuggestionTypeEnum.valueOf(options.type.name),
            query = options.query
        ).map {
            Suggestion(
                id = it.id,
                name = it.name
            )
        }
    }
}
