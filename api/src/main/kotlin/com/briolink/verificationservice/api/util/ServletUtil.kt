package com.briolink.verificationservice.api.util

import mu.KLogging
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class ServletUtil(private val request: HttpServletRequest) {
    fun isIntranet(): Boolean = intranetServerNamePattern.matches(request.serverName)

    companion object : KLogging() {
        val intranetServerNamePattern = "[\\w-]+\\.[\\w-]+\\.svc\\.cluster\\.local$".toRegex()
    }
}
