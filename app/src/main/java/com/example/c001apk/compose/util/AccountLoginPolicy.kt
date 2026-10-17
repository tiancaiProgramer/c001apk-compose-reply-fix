package com.example.c001apk.compose.util

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

/** Never include credentials in toString/logs. */
class AccountLoginCredentials(val uid: String, val username: String, val token: String)

object AccountLoginPolicy {
    const val LOGIN_URL = "https://account.coolapk.com/auth/login?type=mobile"
    private val officialHosts = setOf("account.coolapk.com", "www.coolapk.com", "coolapk.com", "m.coolapk.com")

    private fun officialUrl(url: String): HttpUrl? = url.toHttpUrlOrNull()?.takeIf {
        it.scheme == "https" && it.port == 443 && it.username.isEmpty() && it.password.isEmpty() && it.host in officialHosts
    }

    fun allowsNavigation(url: String): Boolean = officialUrl(url) != null

    fun isSuccessLanding(url: String): Boolean = officialUrl(url)?.let {
        it.host in setOf("www.coolapk.com", "coolapk.com") && it.encodedPath == "/"
    } == true

    fun credentials(cookie: String?): AccountLoginCredentials? {
        if (cookie == null || cookie.length > 32768) return null
        val values = cookie.split(';').mapNotNull { part ->
            val pair = part.trim().split('=', limit = 2)
            if (pair.size == 2) pair[0] to pair[1] else null
        }.toMap()
        fun value(name: String) = values[name]?.takeIf { it.isNotEmpty() && it.length <= 8192 &&
            it.none { char -> char.isWhitespace() || char.code < 32 || char.code == 127 } }
        val uid = value("uid")?.takeIf { it.length <= 32 && it.all { char -> char in '0'..'9' } } ?: return null
        val username = value("username") ?: return null
        val token = value("token") ?: return null
        return AccountLoginCredentials(uid, username, token)
    }

    fun deviceCookie(deviceId: String): String? = deviceId.takeIf {
        it.isNotEmpty() && it.length <= 8192 && it.all { char -> char.code in 33..126 && char !in ";,\"\\" }
    }?.let { "DID=$it; Domain=.coolapk.com; Path=/; Secure" }
}
