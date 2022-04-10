package com.briolink.verificationservice.updater.handler.userservice

import com.briolink.verificationservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.verificationservice.common.jpa.read.entity.CompanyServiceReadEntity
import com.briolink.verificationservice.common.jpa.read.entity.UserServiceReadEntity
import com.briolink.verificationservice.common.jpa.read.entity.UserServiceReadEntityId
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.CompanyServiceReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.ProjectReadRepository
import com.briolink.verificationservice.common.jpa.read.repository.UserServiceReadRepository
import java.time.Instant
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Transactional
@Service
class UserServiceHandlerService(
    private val userServiceReadRepository: UserServiceReadRepository,
    private val companyServiceReadRepository: CompanyServiceReadRepository,
    private val companyReadRepository: CompanyReadRepository,
    private val projectReadRepository: ProjectReadRepository,
) {

    fun createUserService(serviceId: UUID, sellerUserId: UUID): UserServiceReadEntity? {
        return if (!projectReadRepository.existsNotHiddenAndNotDeletedProjectByServiceIdAndUserId(
                serviceId,
                sellerUserId
            )
        ) null
        else userServiceReadRepository.findById(UserServiceReadEntityId(serviceId, sellerUserId)).orElse(
            UserServiceReadEntity(serviceId, sellerUserId).apply {
                val companyServiceRead = companyServiceReadRepository.findById(serviceId)
                    .orElseThrow { throw EntityNotFoundException("CompanyService with id $serviceId not found") }
                val companyRead = companyReadRepository.findById(companyServiceRead.companyId)
                    .orElseThrow { throw EntityNotFoundException("Company with id ${companyServiceRead.companyId} not found") }

                companyId = companyServiceRead.companyId
                serviceName = companyServiceRead.name
                serviceNameTsv = companyServiceRead.name
                companyName = companyRead.name
                companyNameTsv = companyRead.name
                lastUsed = companyServiceRead.lastUsed
                verifiedUsed = companyServiceRead.verifiedUsed
                price = companyServiceRead.data?.price
                serviceData = UserServiceReadEntity.ServiceData(
                    id = companyServiceRead.id!!,
                    name = companyServiceRead.name,
                    slug = companyServiceRead.slug,
                    image = companyServiceRead.data?.image,
                    description = companyServiceRead.data?.description,
                    company = UserServiceReadEntity.Company(
                        id = companyServiceRead.companyId,
                        name = companyRead.name,
                        logo = companyRead.data.logo,
                        slug = companyRead.data.slug
                    )
                )
                userServiceReadRepository.save(this)
            }
        )
    }

    fun updateStatisticUserService(serviceId: UUID, verifiedUsed: Int, lastUsed: Instant?): Int =
        userServiceReadRepository.updateStatsCompanyService(verifiedUsed, lastUsed, serviceId)

    fun updateCompany(companyRead: CompanyReadEntity): Int {
        return userServiceReadRepository.updateCompany(
            companyId = companyRead.id!!,
            slug = companyRead.data.slug,
            name = companyRead.name,
            logo = companyRead.data.logo?.toString()
        )
    }

    fun updateCompanyService(companyServiceRead: CompanyServiceReadEntity): Int {
        return userServiceReadRepository.updateCompanyService(
            serviceId = companyServiceRead.id!!,
            name = companyServiceRead.name,
            slug = companyServiceRead.slug,
            price = companyServiceRead.data?.price,
            lastUsed = companyServiceRead.lastUsed,
            image = companyServiceRead.data?.image?.toString(),
            description = companyServiceRead.data?.description

        )
    }

    fun deleteUserService(serviceId: UUID, sellerUserId: UUID) =
        userServiceReadRepository.deleteByServiceIdAndUserId(serviceId, sellerUserId)

    fun toggleVisibilityUserService(serviceId: UUID, sellerUserId: UUID, hidden: Boolean) =
        userServiceReadRepository.toggleVisibilityUserServiceByServiceIdAndUserId(serviceId, sellerUserId, hidden)
}
