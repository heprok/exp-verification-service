package com.briolink.expverificationservice.api.service.user

import com.briolink.expverificationservice.api.exception.UnavailableException
import com.briolink.expverificationservice.common.jpa.read.entity.UserReadEntity
import com.briolink.expverificationservice.common.jpa.read.repository.UserReadRepository
import com.netflix.graphql.dgs.client.MonoGraphQLClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import java.util.UUID

@Service
class UserService(
    appEndpointsProperties: com.briolink.expverificationservice.common.config.AppEndpointsProperties,
    private val userReadRepository: UserReadRepository
) {
    val webClient = MonoGraphQLClient.createWithWebClient(WebClient.create(appEndpointsProperties.user))

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun createReadUser(entity: UserReadEntity) {
        userReadRepository.save(entity)
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
