package com.example.c001apk.compose.util

import okhttp3.FormBody
import okhttp3.Request
import org.junit.Assert.*
import org.junit.Test

class CommentApiCompatTest {
    private fun request(url: String) = Request.Builder().url(url)
        .header("User-Agent", "Dalvik/test +CoolMarket/13.4.1-2312121-universal")
        .header("X-App-Code", "2312121")
        .header("X-App-Token", "test-token")
        .header("X-App-Device", "test-device")
        .header("Cookie", "test-cookie").build()

    @Test fun commentReadsUseCurrentProfileOnBothHostsAndKeepCursorsAndSession() {
        for (host in listOf("api.coolapk.com", "api2.coolapk.com")) {
            val original = request("https://$host/v6/feed/replyList?id=42&feedType=feed_reply&page=2&lastItem=99")
            val updated = CommentApiCompat.applyTo(original)
            assertEquals("16.6.4", updated.header("X-App-Version"))
            assertEquals("2609291", updated.header("X-App-Code"))
            assertEquals("2609291", updated.header("X-App-Supported"))
            assertEquals("16", updated.header("X-Api-Version"))
            assertEquals("Dalvik/test +CoolMarket/16.6.4-2609291-universal", updated.header("User-Agent"))
            assertEquals(original.url, updated.url)
            for (name in listOf("X-App-Token", "X-App-Device", "Cookie")) {
                assertEquals(original.header(name), updated.header(name))
            }
        }
    }

    @Test fun unrelatedHostsPathsAndWritesKeepTheirConfiguredProfile() {
        for (url in listOf("https://example.com/v6/feed/replyList", "https://api.coolapk.com/v6/feed/detail?id=42")) {
            val original = request(url)
            assertSame(original, CommentApiCompat.applyTo(original))
        }
        val post = request("https://api.coolapk.com/v6/feed/replyList")
            .newBuilder().post(FormBody.Builder().add("message", "test").build()).build()
        assertSame(post, CommentApiCompat.applyTo(post))
        val reply = request("https://api.coolapk.com/v6/feed/reply")
            .newBuilder().post(FormBody.Builder().build()).build()
        assertSame(reply, CommentApiCompat.applyTo(reply))
    }

    @Test fun currentAndNewerProfilesAreNeverDowngraded() {
        for (code in listOf("2609291", "2701011")) {
            val original = request("https://api.coolapk.com/v6/feed/replyList?id=42")
                .newBuilder().header("X-App-Code", code).header("X-Api-Version", "17").build()
            assertSame(original, CommentApiCompat.applyTo(original))
        }
    }
}
