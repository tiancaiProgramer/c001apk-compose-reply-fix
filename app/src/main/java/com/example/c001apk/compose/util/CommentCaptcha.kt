package com.example.c001apk.compose.util

import com.example.c001apk.compose.logic.model.CommentCaptchaChallenge
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Request
import java.net.URLEncoder

/** Human verification only. Never logs, persists, or shares the proof with unrelated requests. */
object CommentCaptcha {
    internal data class Session(val login: Boolean, val uid: String, val token: String, val device: String)
    internal class Attempt internal constructor(internal val revision: Long, internal val session: Session)
    private data class Outcome(val session: Session, val accepted: Boolean)
    private fun session() = Session(CookieUtil.isLogin, CookieUtil.uid, CookieUtil.token, CookieUtil.xAppDevice)
    private data class Proof(val cookie: String, val session: Session, val expires: Long)
    private data class Pending(val challenge: CommentCaptchaChallenge, val session: Session, val result: CompletableDeferred<Boolean>)
    private val mutex = Mutex()
    private val mutableChallenge = MutableStateFlow<CommentCaptchaChallenge?>(null)
    val challenge = mutableChallenge.asStateFlow()
    @Volatile private var pending: Pending? = null
    @Volatile private var proof: Proof? = null
    @Volatile private var revision = 0L
    private var outcome: Outcome? = null // Accessed under mutex.

    internal fun captureAttempt() = Attempt(revision, session())

    internal suspend fun verify(challenge: CommentCaptchaChallenge, attempt: Attempt = captureAttempt()): Boolean = mutex.withLock {
        if (attempt.session != session()) return@withLock false
        // Responses to requests made before the same verification finished must
        // share that result, including cancellation, rather than queue new dialogs.
        if (attempt.revision != revision && outcome?.session == attempt.session) {
            return@withLock outcome?.accepted == true && usableProof() != null
        }
        val request = Pending(challenge, attempt.session, CompletableDeferred())
        proof = null
        pending = request
        mutableChallenge.value = challenge
        var accepted = false
        try {
            accepted = request.result.await()
            accepted
        } finally {
            outcome = Outcome(request.session, accepted)
            revision++
            if (pending === request) {
                pending = null
                mutableChallenge.value = null
            }
        }
    }

    fun complete(challenge: CommentCaptchaChallenge, validate: String?) {
        val request = pending ?: return
        if (request.challenge !== challenge || request.result.isCompleted) return
        val verified = validate?.let(challenge::proof)
        val accepted = verified != null && request.session == session()
        if (accepted) proof = Proof(URLEncoder.encode(verified, "UTF-8"), request.session,
            System.nanoTime() + 5L * 60 * 1_000_000_000)
        request.result.complete(accepted)
    }

    fun applyTo(request: Request): Request {
        if (request.method != "GET" || request.url.scheme != "https" ||
            request.url.host !in setOf("api.coolapk.com", "api2.coolapk.com") ||
            request.url.encodedPath != "/v6/feed/replyList") return request
        val value = usableProof() ?: return request
        val cookie = request.header("Cookie").orEmpty()
        return request.newBuilder().header("Cookie", (if (cookie.isBlank()) "" else "$cookie; ") + "validate=${value.cookie}").build()
    }

    private fun usableProof(): Proof? {
        val value = proof ?: return null
        if (value.session != session() || System.nanoTime() >= value.expires) {
            proof = null
            return null
        }
        return value
    }
}
