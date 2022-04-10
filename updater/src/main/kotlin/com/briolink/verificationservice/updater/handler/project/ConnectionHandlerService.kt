// package com.briolink.verificationservice.updater.handler.connection
//
// import com.briolink.verificationservice.common.domain.v1_0.Connection
// import com.briolink.verificationservice.common.domain.v1_0.ConnectionCompanyRoleType
// import com.briolink.verificationservice.common.jpa.enum.CompanyRoleTypeEnum
// import com.briolink.verificationservice.common.jpa.enum.ProjectStageEnum
// import com.briolink.verificationservice.common.jpa.enum.LastActionByEnum
// import com.briolink.verificationservice.common.jpa.enum.RejectReasonEnum
// import com.briolink.verificationservice.common.jpa.read.entity.CompanyReadEntity
// import com.briolink.verificationservice.common.jpa.read.entity.connection.ConnectionReadEntity
// import com.briolink.verificationservice.common.jpa.read.entity.UserReadEntity
// import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadEntityRepository
// import com.briolink.verificationservice.common.jpa.read.repository.ConnectionReadEntityRepository
// import com.briolink.verificationservice.common.jpa.read.repository.UserReadEntityRepository
// import org.springframework.data.repository.findByIdOrNull
// import org.springframework.stereotype.Service
// import org.springframework.transaction.annotation.Transactional
// import java.util.UUID
// import java.util.stream.Collectors
//
//
// class ConnectionHandlerService(
//    private val companyReadEntityRepository: CompanyReadEntityRepository,
//    private val userReadEntityRepository: UserReadEntityRepository,
//    private val connectionReadEntityRepository: ConnectionReadEntityRepository
// ) {
//    fun createOrUpdate(izd: UUID? = null, data: Connection) {
//        val connection = if (id != null)
//            connectionReadEntityRepository.findByIdOrNull(id) ?: ConnectionReadEntity()
//        else ConnectionReadEntity()
//
//        val participantUsers = userReadEntityRepository.findByIdIsIn(
//            mutableListOf(data.participantFrom.userId!!).apply {
//                if (data.participantTo.userId != null) add(data.participantTo.userId!!)
//            },
//        ).stream().collect(Collectors.toMap(UserReadEntity::id) { v -> v })
//
//        val participantCompanies = mutableListOf<UUID>().apply {
//            if (data.participantFrom.companyId != null) add(data.participantFrom.companyId!!)
//            if (data.participantTo.companyId != null) add(data.participantTo.companyId!!)
//        }.let {
//            if (it.size > 0)
//                companyReadEntityRepository.findByIdIsIn(it).stream().collect(Collectors.toMap(CompanyReadEntity::id) { v -> v })
//            else
//                mapOf()
//        }
//
//        val industry =
//            (
//                if (data.participantFrom.companyRole?.type == ConnectionCompanyRoleType.Seller)
//                    participantCompanies[data.participantFrom.companyId]
//                else participantCompanies[data.participantTo.companyId]
//                )?.data?.industryName
//
//        connectionReadEntityRepository.save(
//            connection.apply {
//                this.id = data.id
//                participantFromUserId = data.participantFrom.userId!!
//                participantFromCompanyId = data.participantFrom.companyId
//                participantToUserId = data.participantTo.userId
//                participantToCompanyId = data.participantTo.companyId
//                status = ProjectStageEnum.fromInt(data.status.value)
//                lastActionBy = LastActionByEnum.fromInt(data.lastActionBy)
//                created = data.created
//                changed = data.changed
//                verified = data.verified
//                this.data = ConnectionReadEntity.Data(
//                    participantFrom = ConnectionReadEntity.Participant(
//                        user = ConnectionReadEntity.User(
//                            id = participantUsers[data.participantFrom.userId]!!.id!!,
//                            slug = participantUsers[data.participantFrom.userId]!!.data.slug,
//                            image = participantUsers[data.participantFrom.userId]!!.data.image,
//                            firstName = participantUsers[data.participantFrom.userId]!!.data.firstName,
//                            lastName = participantUsers[data.participantFrom.userId]!!.data.lastName,
//                        ),
//                        userJobPositionTitle = data.participantFrom.userJobPositionTitle,
//                        company = data.participantFrom.companyId?.let {
//                            ConnectionReadEntity.Company(
//                                id = it,
//                                slug = participantCompanies[data.participantFrom.companyId]!!.data.slug,
//                                name = participantCompanies[data.participantFrom.companyId]!!.name,
//                                logo = participantCompanies[data.participantFrom.companyId]!!.data.logo,
//                            )
//                        },
//                        companyRole = data.participantFrom.companyRole?.let { role ->
//                            ConnectionReadEntity.CompanyRole(
//                                id = role.id,
//                                name = role.name,
//                                type = CompanyRoleTypeEnum.valueOf(role.type.name),
//                            )
//                        },
//                    ),
//                    participantTo = data.participantTo.let {
//                        ConnectionReadEntity.Participant(
//                            user = data.participantTo.userId?.let { toUserId ->
//                                ConnectionReadEntity.User(
//                                    id = participantUsers[toUserId]!!.id!!,
//                                    slug = participantUsers[toUserId]!!.data.slug,
//                                    image = participantUsers[toUserId]!!.data.image,
//                                    firstName = participantUsers[toUserId]!!.data.firstName,
//                                    lastName = participantUsers[toUserId]!!.data.lastName,
//                                )
//                            },
//                            userJobPositionTitle = it.userJobPositionTitle,
//                            company = data.participantTo.companyId?.let { toCompanyId ->
//                                ConnectionReadEntity.Company(
//                                    id = toCompanyId,
//                                    slug = participantCompanies[toCompanyId]!!.data.slug,
//                                    name = participantCompanies[toCompanyId]!!.name,
//                                    logo = participantCompanies[toCompanyId]!!.data.logo,
//                                )
//                            },
//                            companyRole = it.companyRole?.let { role ->
//                                ConnectionReadEntity.CompanyRole(
//                                    id = role.id,
//                                    name = role.name,
//                                    type = CompanyRoleTypeEnum.valueOf(role.type.name),
//                                )
//                            },
//                        )
//                    },
//                    services = data.services.map {
//                        ConnectionReadEntity.Service(
//                            id = it.id,
//                            serviceId = it.serviceId,
//                            serviceName = it.serviceName,
//                            startDate = it.startDate,
//                            endDate = it.endDate,
//                        )
//                    },
//                    industry = industry,
//                    rejectReason = data.rejectReason?.let { RejectReasonEnum.valueOf(it) },
//                    rejectComment = data.rejectComment
//                )
//            }
//        )
//    }
//
//    fun deleteById(id: UUID, userId: UUID, completely: Boolean) {
//        if (completely && connectionReadEntityRepository.existsById(id))
//            connectionReadEntityRepository.deleteById(id)
//    }
// }
