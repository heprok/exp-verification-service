package com.briolink.verificationservice.api.dataloader

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
) : DataLoader() {
    override fun loadData() {
        if (companyReadRepository.count().toInt() == 0
        ) {
            val companyReadEntityList: MutableList<CompanyReadEntity> = mutableListOf()

            companyReadEntityList.add(
                CompanyReadEntity(id = UUID.randomUUID()).apply {
                    data = CompanyReadEntity.CompanyData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "PayPal",
                        logo = URL("https://www.paypalobjects.com/webstatic/icon/pp258.png"),
                    )
                },
            )
            companyReadEntityList.add(
                CompanyReadEntity(id = UUID.randomUUID()).apply {
                    data = CompanyReadEntity.CompanyData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Nikonv&Sheshuk",
                        logo = null,
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity(id = UUID.randomUUID()).apply {
                    data = CompanyReadEntity.CompanyData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Nokia",
                        logo = null,
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity(id = UUID.randomUUID()).apply {
                    data = CompanyReadEntity.CompanyData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Amazon",
                        logo = URL("https://regnum.ru/uploads/pictures/news/2018/11/30/regnum_picture_154356037138044_normal.png"),
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity(id = UUID.randomUUID()).apply {
                    data = CompanyReadEntity.CompanyData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Notion Labs INC",
                        logo = URL("https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png"),
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity(id = UUID.randomUUID()).apply {
                    data = CompanyReadEntity.CompanyData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Google",
                        logo = URL("https://w7.pngwing.com/pngs/760/624/png-transparent-google-logo-google-search-advertising-google-company-text-trademark.png"), // ktlint-disable max-line-length
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity(id = UUID.randomUUID()).apply {
                    data = CompanyReadEntity.CompanyData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Twitter",
                        logo = URL("https://upload.wikimedia.org/wikipedia/ru/thumb/9/9f/Twitter_bird_logo_2012.svg/1261px-Twitter_bird_logo_2012.svg.png"), // ktlint-disable max-line-length
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity(id = UUID.randomUUID()).apply {
                    data = CompanyReadEntity.CompanyData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "GitHab",
                        logo = URL("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"),
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity(id = UUID.randomUUID()).apply {
                    data = CompanyReadEntity.CompanyData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Walmart",
                        logo = URL("https://logo.clearbit.com/www.walmart.com/"),
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity(id = UUID.randomUUID()).apply {
                    data = CompanyReadEntity.CompanyData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Oaktree Capital",
                        logo = URL("https://logo.clearbit.com/www.oaktreecapital.com/"),
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity(id = UUID.randomUUID()).apply {
                    data = CompanyReadEntity.CompanyData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "KFC",
                        logo = URL("https://logo.clearbit.com/www.kfc.com/"),
                    )
                }
            )
            companyReadEntityList.add(
                CompanyReadEntity(id = UUID.randomUUID()).apply {
                    data = CompanyReadEntity.CompanyData(
                        id = UUID.randomUUID(),
                        slug = UUID.randomUUID().toString(),
                        name = "Tata",
                        logo = URL("https://logo.clearbit.com/www.tata.com/"),
                    )
                }
            )
            companyReadRepository.saveAll(companyReadEntityList)
        }
    }
}
