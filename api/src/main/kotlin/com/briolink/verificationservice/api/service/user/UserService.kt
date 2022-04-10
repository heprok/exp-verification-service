package com.briolink.verificationservice.api.service.user

import com.briolink.verificationservice.api.exception.UnavailableException
import com.briolink.verificationservice.api.service.user.dto.CreateUserResultDto
import com.briolink.verificationservice.common.jpa.read.entity.UserReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.UserReadRepository
import com.netflix.graphql.dgs.client.MonoGraphQLClient
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import java.util.UUID

@Service
class UserService(
    appEndpointsProperties: com.briolink.verificationservice.common.config.AppEndpointsProperties,
    private val userReadRepository: UserReadRepository
) {
    val webClient = MonoGraphQLClient.createWithWebClient(WebClient.create(appEndpointsProperties.user))

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun createReadUser(entity: UserReadEntity) {
        userReadRepository.save(entity)
    }

    @Throws(UnavailableException::class)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun create(firstName: String, lastName: String, personalEmail: String, kcId: UUID? = null): CreateUserResultDto {
        val result = webClient.reactiveExecuteQuery(
            """
            mutation createUser(${'$'}input: CreateUserInput!) {
              createUser(input: ${'$'}input) {
                data {
                  id
                  slug
                  personalEmail
                  registered
                }
              }
            }
            """,
            mapOf(
                "input" to mapOf(
                    "kcId" to kcId,
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "personalEmail" to personalEmail,
                ),
            ),
        ).block() ?: throw UnavailableException("Unavailable user service")

        return try {
            val resultDto = CreateUserResultDto(
                id = UUID.fromString(result.extractValue<String>("createUser.data.id")),
                registered = result.extractValue("createUser.data.registered"),
                slug = result.extractValue("createUser.data.slug"),
                personalEmail = result.extractValue("createUser.data.personalEmail")
            )

            // todo: add user profile image
            try {
                createReadUser(
                    UserReadEntity().apply {
                        this.id = resultDto.id
                        name = "$firstName $lastName"
                        this.personalEmail = resultDto.personalEmail
                        this.data = UserReadEntity.Data(
                            id = resultDto.id,
                            slug = resultDto.slug,
                            firstName = firstName,
                            lastName = lastName,
                            currentJobPosition = null,
                            image = null,
                            description = null,
                            location = null,
                            verifiedConnections = 0,
                            verifiedProjects = 0
                        )
                    }
                )
            } catch (_: DataIntegrityViolationException) { }

            resultDto
        } catch (e: Exception) {
            throw UnavailableException("Unavailable user service")
        }
    }

    @Throws(UnavailableException::class)
    fun createUserJobPositionIfNotExist(userId: UUID, companyId: UUID, title: String): Boolean {
        val result = webClient.reactiveExecuteQuery(
            """
            mutation createUserJobPositionIfNotExist(${'$'}userId: ID!, ${'$'}companyId: ID!, ${'$'}title: String!) {
              createUserJobPositionIfNotExist(userId: ${'$'}userId, companyId: ${'$'}companyId, title: ${'$'}title) {
                success
              }
            }
            """,
            mapOf(
                "userId" to userId.toString(),
                "companyId" to companyId.toString(),
                "title" to title,
            ),
        ).block() ?: throw UnavailableException("Unavailable user service")

        return try {
            result.extractValue("createUserJobPositionIfNotExist.success")
        } catch (e: Exception) {
            throw UnavailableException("Unavailable user service")
        }
    }
}
