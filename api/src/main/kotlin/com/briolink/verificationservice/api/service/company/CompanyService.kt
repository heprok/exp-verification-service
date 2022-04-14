// package com.briolink.verificationservice.api.service.company
//
// import com.briolink.lib.location.model.LocationId
// import com.briolink.lib.location.model.LocationMinInfo
// import com.briolink.lib.location.service.LocationService
// import com.briolink.verificationservice.api.exception.UnavailableException
// import com.briolink.verificationservice.api.validation.ValidWebsiteOrNull
// import com.briolink.verificationservice.common.jpa.read.entity.CompanyReadEntity
// import com.briolink.verificationservice.common.jpa.read.entity.CompanyServiceReadEntity
// import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
// import com.briolink.verificationservice.common.jpa.read.repository.CompanyServiceReadRepository
// import com.netflix.graphql.dgs.client.MonoGraphQLClient
// import java.net.URL
// import java.util.UUID
// import mu.KLogging
// import org.springframework.dao.DataIntegrityViolationException
// import org.springframework.data.repository.findByIdOrNull
// import org.springframework.stereotype.Service
// import org.springframework.transaction.annotation.Propagation
// import org.springframework.transaction.annotation.Transactional
// import org.springframework.validation.annotation.Validated
// import org.springframework.web.reactive.function.client.WebClient
// import javax.validation.constraints.NotEmpty
//
// @Service
// @Validated
// class CompanyService(
//     val companyReadRepository: CompanyReadRepository,
//     val companyServiceReadRepository: CompanyServiceReadRepository,
//     private val locationService: LocationService,
//     appEndpointsProperties: com.briolink.verificationservice.common.config.AppEndpointsProperties
// ) {
//     companion object : KLogging()w
//
//     val webClientCompany = MonoGraphQLClient.createWithWebClient(WebClient.create(appEndpointsProperties.company))
//     val webClientCompanyService =
//         MonoGraphQLClient.createWithWebClient(WebClient.create(appEndpointsProperties.companyservice))
//
//     fun getById(id: UUID) = companyReadRepository.findByIdOrNull(id)
//
//     @Transactional(propagation = Propagation.REQUIRES_NEW)
//     fun createReadCompany(entity: CompanyReadEntity) {
//         companyReadRepository.save(entity)
//     }
//
//     @Transactional(propagation = Propagation.REQUIRES_NEW)
//     fun createReadCompanyService(entity: CompanyServiceReadEntity) {
//         companyServiceReadRepository.save(entity)
//     }
//
//     @Transactional(propagation = Propagation.NOT_SUPPORTED)
//     fun create(
//         createByUserId: UUID,
//         @NotEmpty name: String,
//         @ValidWebsiteOrNull website: String?,
//         logoUrl: String? = null,
//         description: String? = null
//     ): CompanyReadEntity? =
//         webClientCompany.reactiveExecuteQuery(
//             """
//             mutation createCompany(${'$'}input: CreateCompanyInput!) {
//               createCompany(input: ${'$'}input) {
//                   id
//                   slug
//                   logo {
//                     url
//                   }
//                   industry {
//                     id
//                     name
//                   }
//                   occupation {
//                     id
//                     name
//                   }
//               }
//             }
//             """,
//             mapOf(
//                 "input" to mapOf(
//                     "name" to name,
//                     "website" to website,
//                     "logo" to logoUrl,
//                     "description" to description,
//                     "createBy" to createByUserId.toString()
//                 ),
//             ),
//         ).block()?.let {
//             if (it.hasErrors()) {
//                 logger.error { it.errors }
//                 return null
//             }
//
//             return try {
//                 val id = UUID.fromString(it.extractValue<String>("createCompany.id"))
//                 val slug = it.extractValue<String>("createCompany.slug")
//                 val logo = try {
//                     URL(it.extractValue<String>("createCompany.logo.url"))
//                 } catch (e: Exception) {
//                     null
//                 }
//                 val description = it.extractValue<String>("createCompany.description")
//                 val industryId = try {
//                     UUID.fromString(it.extractValue<String>("createCompany.industry.id"))
//                 } catch (e: Exception) {
//                     null
//                 }
//                 val industryName = try {
//                     it.extractValue<String>("createCompany.industry.name")
//                 } catch (e: Exception) {
//                     null
//                 }
//                 val occupationId = try {
//                     UUID.fromString(it.extractValue<String>("createCompany.occupation.id"))
//                 } catch (e: Exception) {
//                     null
//                 }
//                 val occupationName = try {
//                     it.extractValue<String>("createCompany.occupation.name")
//                 } catch (e: Exception) {
//                     null
//                 }
//                 val locationId = try {
//                     LocationId.fromString(it.extractValue("createCompany.location"))
//                 } catch (e: Exception) {
//                     null
//                 }
//
//                 val company = CompanyReadEntity().apply {
//                     this.id = id
//                     this.name = name
//                     this.data = CompanyReadEntity.Data(
//                         id = id,
//                         name = name,
//                         slug = slug,
//                         logo = logo,
//                         website = website?.let { URL(website) },
//                         industryId = industryId,
//                         industryName = industryName,
//                         marketSegmentId = occupationId,
//                         marketSegmentName = occupationName,
//                         location = locationId?.let { id -> locationService.getLocationInfo(id, LocationMinInfo::class.java) },
//                         description = description,
//                         verifiedConnections = 0,
//                         verifiedProjects = 0
//                     )
//                 }
//
//                 try {
//                     createReadCompany(company)
//                 } catch (_: DataIntegrityViolationException) {
//                 }
//
//                 company
//             } catch (e: Exception) {
//                 logger.error { e }
//                 null
//             }
//         }
//
//     @Transactional(propagation = Propagation.NOT_SUPPORTED)
//     fun createService(companyId: UUID, name: String): UUID? =
//         webClientCompanyService.reactiveExecuteQuery(
//             """
//             mutation createServiceLocal(${'$'}companyId: ID!, ${'$'}name: String!) {
//               createServiceLocal(companyId: ${'$'}companyId, name: ${'$'}name) {
//                 data {
//                   id
//                   slug
//                   price
//                   logo {
//                         url
//                     }
//                   description
//                 }
//               }
//             }
//             """,
//             mapOf(
//                 "companyId" to companyId.toString(),
//                 "name" to name
//             ),
//         ).block()?.let {
//             if (it.hasErrors()) {
//                 logger.error { it.errors }
//                 return null
//             }
//
//             return try {
//                 val id = UUID.fromString(it.extractValue<String>("createServiceLocal.data.id"))
//                 val slug = it.extractValue<String>("createServiceLocal.data.slug")
//                 val description = try {
//                     it.extractValue<String>("createServiceLocal.data.description")
//                 } catch (e: Exception) {
//                     null
//                 }
//                 val imageUrl = try {
//                     URL(it.extractValue<String>("createServiceLocal.data.logo.url"))
//                 } catch (e: Exception) {
//                     null
//                 }
//                 val price = try {
//                     it.extractValue<Double>("createServiceLocal.data.price")
//                 } catch (e: Exception) {
//                     null
//                 }
//
//                 try {
//                     createReadCompanyService(
//                         CompanyServiceReadEntity().apply {
//                             this.id = id
//                             this.name = name
//                             this.slug = slug
//                             this.companyId = companyId
//                             this.data = CompanyServiceReadEntity.Data(
//                                 id = id,
//                                 slug = slug,
//                                 image = imageUrl,
//                                 name = name,
//                                 description = description,
//                                 price = price?.toFloat()
//                             )
//                         }
//                     )
//                 } catch (_: DataIntegrityViolationException) {
//                 }
//
//                 id
//             } catch (e: Exception) {
//                 logger.error { e }
//                 null
//             }
//         }
//
//     fun toggleVisibilityCompanyService(serviceId: UUID, hidden: Boolean = false): Boolean {
//         val result = webClientCompanyService.reactiveExecuteQuery(
//             """
//                 mutation hideServiceLocal(${'$'}serviceId: ID!, ${'$'}hidden: Boolean!) {
//                   hideServiceLocal(serviceId: ${'$'}serviceId, hidden: ${'$'}hidden ) {
//                     success
//                   }
//                 }
//             """,
//             mapOf(
//                 "serviceId" to serviceId,
//                 "hidden" to hidden
//             ),
//         ).block() ?: throw UnavailableException("CompanyService service unavailable")
//
//         return try {
//             val success = result.extractValue<Boolean>("hideServiceLocal.success")
//             success
//         } catch (e: Exception) {
//             logger.error { e }
//             false
//         }
//     }
// }
