package com.briolink.verificationservice.api.util

import java.net.URL

fun parseUrl(url: String): URL? = try {
    val uri = if (url.startsWith("http://") || url.startsWith("https://")) URL(url) else URL("https://$url")
    uri
} catch (e: Exception) {
    null
}
