package com.briolink.expverificationservice.api.service.suggestion.dto

enum class SuggestionGroupEnum {
    ConnectionWithUser,
    ConnectionWithCompany,
    Project,
    UserService
}

enum class SuggestionTypeEnum {
    UserFullName,
    UserCurrentJobPositionTitle,
    UserCurrentJobPositionCompanyName,
    CompanyName,
    CompanyIndustry,
    CompanyMarketSegment,
    LocationName,
    CollaboratorCompanyIndustryName,
    CollaboratorCompanyName,
    CollaboratorRoleName,
    ServiceName,
    UserCompanyName,
    Position,
    ProviderCompanyName
}
