package com.example.c001apk.compose.util

import okhttp3.Request

/** Legacy comment profiles were retired server-side; retain newer configured profiles. */
internal object CommentApiCompat {
    private const val VERSION_NAME = "16.6.4"
    private const val VERSION_CODE = "2609291"
    private val clientSuffix = Regex("(?<=\\+CoolMarket/)[^\\s]+")

    fun applyTo(request: Request): Request {
        if (request.method != "GET" ||
            request.url.host !in setOf("api.coolapk.com", "api2.coolapk.com") ||
            request.url.encodedPath != "/v6/feed/replyList") return request
        val configuredCode = request.header("X-App-Code")?.toLongOrNull() ?: return request
        if (configuredCode >= VERSION_CODE.toLong()) return request

        return request.newBuilder()
            .header("X-App-Version", VERSION_NAME)
            .header("X-App-Code", VERSION_CODE)
            .header("X-App-Supported", VERSION_CODE)
            .header("X-Api-Version", "16")
            .apply {
                request.header("User-Agent")?.let { agent ->
                    header("User-Agent", clientSuffix.replace(agent,
                        "$VERSION_NAME-$VERSION_CODE-universal"))
                }
            }.build()
    }
}
