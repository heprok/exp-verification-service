package com.briolink.verificationservice.api.dataloader

import com.briolink.verificationservice.api.service.company.CompanyService
import com.briolink.verificationservice.common.jpa.read.entity.CompanyReadEntity
import com.briolink.verificationservice.common.jpa.read.repository.CompanyReadRepository
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.net.URL
import java.util.UUID

@Component
@Order(1)
class CompanyDataLoader(
    private val companyReadRepository: CompanyReadRepository,
    private val companyService: CompanyService,
) : DataLoader() {
    override fun loadData() {
        if (companyReadRepository.count().toInt() == 0
        ) {
            val companyReadEntityList: MutableList<CompanyReadEntity> = mutableListOf()

            companyReadEntityList.add(
                CompanyReadEntity().apply {
                    id = UUID.randomUUID()
                    name = "PayPal"
                    data = CompanyReadEntity.Data(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        logo = URL("https://www.paypalobjects.com/webstatic/icon/pp258.png"),
                        website = URL("https://www.paypal.com"),
                        name = "PayPal",
                        industryId = null,
                        industryName = null,
                        marketSegmentId = null,
                        marketSegmentName = null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                },
            )
            companyReadEntityList.add(
                CompanyReadEntity().apply {
                    id = UUID.randomUUID()
                    name = "Nikonv&Sheshuk"
                    data = CompanyReadEntity.Data(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        logo = null,
                        name = "Nikonv&Sheshuk",
                        website = null,
                        industryId = null,
                        industryName = null,
                        marketSegmentId = null,
                        marketSegmentName = null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity().apply {
                    id = UUID.randomUUID()
                    name = "Nokia"
                    data = CompanyReadEntity.Data(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        logo = null,
                        name = "Nokia",
                        website = null,
                        industryId = null,
                        industryName = null,
                        marketSegmentId = null,
                        marketSegmentName = null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity().apply {
                    id = UUID.randomUUID()
                    name = "Amazon"
                    data = CompanyReadEntity.Data(
                        name = "Amazon",
                        logo = URL("https://regnum.ru/uploads/pictures/news/2018/11/30/regnum_picture_154356037138044_normal.png"),
                        website = URL("https://www.amazon.com/"),
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        industryId = null,
                        industryName = null,
                        marketSegmentId = null,
                        marketSegmentName = null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity().apply {
                    id = UUID.randomUUID()
                    name = "Notion Labs INC"
                    data = CompanyReadEntity.Data(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Notion Labs INC",
                        website = URL("https://www.notion.so/"),
                        logo = URL("https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png"),
                        industryId = null,
                        industryName = null,
                        marketSegmentId = null,
                        marketSegmentName = null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity().apply {
                    id = UUID.randomUUID()
                    name = "Google"
                    data = CompanyReadEntity.Data(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Google",
                        website = URL("https://www.google.com/"),
                        logo = URL("https://w7.pngwing.com/pngs/760/624/png-transparent-google-logo-google-search-advertising-google-company-text-trademark.png"), // ktlint-disable max-line-length
                        industryId = null,
                        industryName = null,
                        marketSegmentId = null,
                        marketSegmentName = null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity().apply {
                    id = UUID.randomUUID()
                    name = "Twitter"
                    data = CompanyReadEntity.Data(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Twitter",
                        website = URL("https://twitter.com/"),
                        logo = URL("https://upload.wikimedia.org/wikipedia/ru/thumb/9/9f/Twitter_bird_logo_2012.svg/1261px-Twitter_bird_logo_2012.svg.png"), // ktlint-disable max-line-length
                        industryId = null,
                        industryName = null,
                        marketSegmentId = null,
                        marketSegmentName = null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity().apply {
                    id = UUID.randomUUID()
                    name = "GitHab"
                    data = CompanyReadEntity.Data(
                        id = UUID.randomUUID(),
                        name = "GitHab",
                        website = URL("https://github.com/"),
                        slug = UUID.randomUUID().toString(),
                        logo = URL("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"),
                        industryId = null,
                        industryName = null,
                        marketSegmentId = null,
                        marketSegmentName = null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity().apply {
                    id = UUID.randomUUID()
                    name = "Walmart"
                    data = CompanyReadEntity.Data(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        website = URL("https://www.walmart.com/"),
                        name = "Walmart",
                        logo = URL("https://logo.clearbit.com/www.walmart.com/"),
                        industryId = null,
                        industryName = null,
                        marketSegmentId = null,
                        marketSegmentName = null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity().apply {
                    id = UUID.randomUUID()
                    name = "Oaktree Capital"
                    data = CompanyReadEntity.Data(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Oaktree Capital",
                        logo = URL("https://logo.clearbit.com/www.oaktreecapital.com/"),
                        website = URL("https://www.oaktreecapital.com/"),
                        industryId = null,
                        industryName = null,
                        marketSegmentId = null,
                        marketSegmentName = null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity().apply {
                    id = UUID.randomUUID()
                    name = "KFC"
                    data = CompanyReadEntity.Data(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "KFC",
                        website = URL("https://www.kfc.com/"),
                        logo = URL("https://logo.clearbit.com/www.kfc.com/"),
                        industryId = null,
                        industryName = null,
                        marketSegmentId = null,
                        marketSegmentName = null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity().apply {
                    id = UUID.randomUUID()
                    name = "Tata"
                    data = CompanyReadEntity.Data(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Tata",
                        website = URL("http://www.tata.com"),
                        logo = URL("https://logo.clearbit.com/www.tata.com/"),
                        industryId = null,
                        industryName = null,
                        marketSegmentId = null,
                        marketSegmentName = null,
                        description = null,
                        location = null,
                        verifiedProjects = 0,
                        verifiedConnections = 0,
                    )
                }
            )

            companyReadEntityList.forEach {
                companyService.createReadCompany(it)
            }
        }
    }
}
