package com.briolink.expverificationservice.updater.dataloader

import com.briolink.expverificationservice.common.dataloader.DataLoader
import com.briolink.expverificationservice.common.jpa.read.repository.CompanyReadRepository
import com.briolink.expverificationservice.updater.handler.company.CompanyEventData
import com.briolink.expverificationservice.updater.handler.company.CompanyHandlerService
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.net.URL
import java.util.UUID

@Component
@Order(1)
class CompanyDataLoader(
    private val companyReadRepository: CompanyReadRepository,
    private val companyHandlerService: CompanyHandlerService
) : DataLoader() {
    override fun loadData() {
        if (companyReadRepository.count().toInt() == 0
        ) {
            val companyReadEntityList: MutableList<CompanyEventData> = mutableListOf()
            companyReadEntityList.add(
                CompanyEventData(
                    id = UUID.randomUUID(),
                    slug = "PayPal",
                    name = "PayPal",
                    logo = URL("https://www.paypalobjects.com/webstatic/icon/pp258.png"),
                )
            )
            companyReadEntityList.add(
                CompanyEventData(
                    id = UUID.randomUUID(),
                    slug = "Nikonv&Sheshuk",
                    name = "Nikonv&Sheshuk",
                    logo = null,
                )
            )
            companyReadEntityList.add(
                CompanyEventData(
                    id = UUID.randomUUID(),
                    slug = "Nokia",
                    name = "Nokia",
                    logo = null,
                )
            )
            companyReadEntityList.add(
                CompanyEventData(
                    id = UUID.randomUUID(),
                    slug = "Amazon",
                    name = "Amazon",
                    logo = URL("https://regnum.ru/uploads/pictures/news/2018/11/30/regnum_picture_154356037138044_normal.png"),
                )
            )

            companyReadEntityList.add(
                CompanyEventData(
                    id = UUID.randomUUID(),
                    slug = "Notion Labs INC",
                    name = "Notion Labs INC",
                    logo = URL("https://upload.wikimedia.org/wikipedia/commons/4/45/Notion_app_logo.png"),
                )
            )
            companyReadEntityList.add(
                CompanyEventData(
                    id = UUID.randomUUID(),
                    slug = "Google",
                    name = "Google",
                    logo = URL("https://w7.pngwing.com/pngs/760/624/png-transparent-google-logo-google-search-advertising-google-company-text-trademark.png"), // ktlint-disable max-line-length
                )
            )
            companyReadEntityList.add(
                CompanyEventData(
                    id = UUID.randomUUID(),
                    slug = "Twitter",
                    name = "Twitter",
                    logo = URL("https://upload.wikimedia.org/wikipedia/ru/thumb/9/9f/Twitter_bird_logo_2012.svg/1261px-Twitter_bird_logo_2012.svg.png"), // ktlint-disable max-line-length
                )
            )
            companyReadEntityList.add(
                CompanyEventData(
                    id = UUID.randomUUID(),
                    slug = "GitHab",
                    name = "GitHab",
                    logo = URL("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"),
                )
            )
            companyReadEntityList.add(
                CompanyEventData(
                    id = UUID.randomUUID(),
                    slug = "Walmart",
                    name = "Walmart",
                    logo = URL("https://logo.clearbit.com/www.walmart.com/"),
                )
            )
            companyReadEntityList.add(
                CompanyEventData(
                    id = UUID.randomUUID(),
                    slug = "Oaktree Capital",
                    name = "Oaktree Capital",
                    logo = URL("https://logo.clearbit.com/www.oaktreecapital.com/"),
                )
            )
            companyReadEntityList.add(
                CompanyEventData(
                    id = UUID.randomUUID(),
                    slug = "KFC",
                    name = "KFC",
                    logo = URL("https://logo.clearbit.com/www.kfc.com/"),
                )
            )
            companyReadEntityList.add(
                CompanyEventData(
                    id = UUID.randomUUID(),
                    slug = "Tata",
                    name = "Tata",
                    logo = URL("https://logo.clearbit.com/www.tata.com/"),
                )
            )

            companyReadEntityList.forEachIndexed { index, companyEventData ->
                if (index == COUNT_COMPANY) return@forEachIndexed
                companyHandlerService.createOrUpdate(companyEventData)
            }
        }
    }
}
