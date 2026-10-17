package com.example.c001apk.compose.util

import com.example.c001apk.compose.logic.model.CommentCaptchaChallenge
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import okhttp3.Request
import org.junit.After
import org.junit.Assert.*
import org.junit.Test

class CommentCaptchaTest {
    private val originalDevice = CookieUtil.xAppDevice
    private val originalToken = CookieUtil.token
    private val challenge = CommentCaptchaChallenge("1234567890abcdef1234567890abcdef")
    private fun request(url: String = "https://api.coolapk.com/v6/feed/replyList?id=42&page=2") =
        Request.Builder().url(url).header("Cookie", "test-session=unchanged").build()

    @After fun restore() {
        CookieUtil.xAppDevice = originalDevice
        CookieUtil.token = originalToken
    }

    @Test fun challengeSupportsObjectAndEncodedJsonButRejectsUnknownProtocols() {
        val extra = """{"captchaType":"NEC","captchaId":"${challenge.captchaId}","captchaField":""}"""
        for (value in listOf(extra, Gson().toJson(extra))) {
            val response = Gson().fromJson("""{"code":403,"messageStatus":"err_request_captcha_v2","messageExtra":$value}""", HomeFeedResponse::class.java)
            assertEquals(403, response.status)
            assertEquals(challenge.captchaId, CommentCaptchaChallenge.from(response)?.captchaId)
        }
        for (value in listOf(extra.replace("NEC", "other"), extra.replace("\"captchaField\":\"\"", "\"captchaField\":\"_v2_post_token\""), "\"invalid-json\"")) {
            val response = Gson().fromJson("""{"messageStatus":"err_request_captcha_v2","messageExtra":$value}""", HomeFeedResponse::class.java)
            assertNull(CommentCaptchaChallenge.from(response))
        }
    }

    @Test fun proofRequiresAnActualNonemptySafeProviderResult() {
        assertEquals("NEC:12345678:human-result", challenge.proof("human-result"))
        for (value in listOf("", "x;other=cookie", "x\r\ny", "has spaces", "x".repeat(8193))) {
            assertNull(challenge.proof(value))
        }
    }

    @Test fun verifiedReadPreservesQueryAndSessionAndNeverChangesWritesOrOtherHosts() = runBlocking {
        val verification = launch(start = CoroutineStart.UNDISPATCHED) { assertTrue(CommentCaptcha.verify(challenge)) }
        assertSame(challenge, CommentCaptcha.challenge.value)
        CommentCaptcha.complete(challenge, "human-result")
        verification.join()
        val original = request()
        val updated = CommentCaptcha.applyTo(original)
        assertEquals(original.url, updated.url)
        assertEquals("test-session=unchanged; validate=NEC%3A12345678%3Ahuman-result", updated.header("Cookie"))
        assertTrue(CommentCaptcha.applyTo(request("https://api2.coolapk.com/v6/feed/replyList?id=42")).header("Cookie")!!.contains("validate="))
        for (url in listOf("http://api.coolapk.com/v6/feed/replyList", "https://example.com/v6/feed/replyList", "https://api.coolapk.com/v6/main/indexV8")) {
            val unrelated = request(url)
            assertSame(unrelated, CommentCaptcha.applyTo(unrelated))
        }
        val post = request().newBuilder().post(okhttp3.FormBody.Builder().build()).build()
        assertSame(post, CommentCaptcha.applyTo(post))
        CookieUtil.xAppDevice = originalDevice + "-changed"
        assertSame(original, CommentCaptcha.applyTo(original))
    }

    @Test fun cancellationAndAccountChangeCannotAcceptAProof() = runBlocking {
        val cancelled = launch(start = CoroutineStart.UNDISPATCHED) { assertFalse(CommentCaptcha.verify(challenge)) }
        CommentCaptcha.complete(challenge, null)
        cancelled.join()
        assertNull(CommentCaptcha.challenge.value)
        val original = request()
        assertSame(original, CommentCaptcha.applyTo(original))
        val changed = launch(start = CoroutineStart.UNDISPATCHED) { assertFalse(CommentCaptcha.verify(challenge)) }
        CookieUtil.token = originalToken + "-changed"
        CommentCaptcha.complete(challenge, "human-result")
        changed.join()
        assertSame(original, CommentCaptcha.applyTo(original))
    }

    @Test fun concurrentChallengesShareOneHumanVerification() = runBlocking {
        withTimeout(2000) {
            val attempt = CommentCaptcha.captureAttempt()
            val first = launch(start = CoroutineStart.UNDISPATCHED) {
                assertTrue(CommentCaptcha.verify(challenge, attempt))
            }
            val otherChallenge = CommentCaptchaChallenge(challenge.captchaId)
            val second = launch(start = CoroutineStart.UNDISPATCHED) {
                assertTrue(CommentCaptcha.verify(otherChallenge, attempt))
            }
            CommentCaptcha.complete(challenge, "human-result")
            first.join()
            second.join()
            assertNull(CommentCaptcha.challenge.value)
            assertTrue(CommentCaptcha.applyTo(request()).header("Cookie")!!.contains("validate="))
        }
    }

    @Test fun concurrentChallengesDoNotReopenAfterUserCancels() = runBlocking {
        withTimeout(2000) {
            val attempt = CommentCaptcha.captureAttempt()
            val first = launch(start = CoroutineStart.UNDISPATCHED) {
                assertFalse(CommentCaptcha.verify(challenge, attempt))
            }
            val second = launch(start = CoroutineStart.UNDISPATCHED) {
                assertFalse(CommentCaptcha.verify(CommentCaptchaChallenge(challenge.captchaId), attempt))
            }
            CommentCaptcha.complete(challenge, null)
            first.join()
            second.join()
            assertNull(CommentCaptcha.challenge.value)
            // A new user request may still display the server's new challenge.
            val retry = launch(start = CoroutineStart.UNDISPATCHED) {
                assertFalse(CommentCaptcha.verify(challenge, CommentCaptcha.captureAttempt()))
            }
            assertSame(challenge, CommentCaptcha.challenge.value)
            CommentCaptcha.complete(challenge, null)
            retry.join()
        }
    }

    @Test fun oldAccountRequestsCannotOpenVerificationInANewSession() = runBlocking {
        val attempt = CommentCaptcha.captureAttempt()
        CookieUtil.token = originalToken + "-new-session"
        assertFalse(CommentCaptcha.verify(challenge, attempt))
        assertNull(CommentCaptcha.challenge.value)
    }
}
