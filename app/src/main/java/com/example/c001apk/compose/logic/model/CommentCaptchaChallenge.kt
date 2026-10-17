package com.example.c001apk.compose.logic.model

import com.google.gson.JsonParser

/** Only protocol configuration is exposed to the verification UI; proofs stay in memory. */
class CommentCaptchaChallenge(val captchaId: String) {
    fun proof(validate: String): String? {
        if (validate.isEmpty() || validate.length > 8192 || validate.any { it.isWhitespace() || it.code < 32 || it == ';' }) return null
        return "NEC:${captchaId.take(8)}:$validate"
    }

    companion object {
        fun from(response: HomeFeedResponse): CommentCaptchaChallenge? = runCatching {
            if (response.messageStatus != "err_request_captcha_v2") return null
            val raw = response.messageExtra ?: return null
            val extra = (if (raw.isJsonPrimitive && raw.asJsonPrimitive.isString)
                JsonParser.parseString(raw.asString) else raw).asJsonObject
            if (extra["captchaType"]?.asString != "NEC" || extra["captchaField"]?.asString != "") return null
            val id = extra["captchaId"]?.asString ?: return null
            if (!Regex("^[a-zA-Z0-9]{16,64}$").matches(id)) return null
            CommentCaptchaChallenge(id)
        }.getOrNull()
    }
}
