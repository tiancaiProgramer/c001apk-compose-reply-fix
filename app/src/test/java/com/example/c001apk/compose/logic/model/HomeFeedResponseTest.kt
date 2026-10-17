package com.example.c001apk.compose.logic.model

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class HomeFeedResponseTest {
    private val gson = Gson()

    @Test fun stringMessageStatusKeepsTheServerExplanation() {
        val response = gson.fromJson(
            """{"status":403,"message":"当前访问需要验证码","messageStatus":"err_request_captcha_v2","data":null}""",
            HomeFeedResponse::class.java,
        )
        assertEquals("当前访问需要验证码", response.message)
        assertEquals("err_request_captcha_v2", response.messageStatus)
        assertNull(response.data)
    }

    @Test fun numericAndMissingStatusRemainCompatibleWithNormalLists() {
        val response = gson.fromJson("""{"messageStatus":0,"data":[]}""", HomeFeedResponse::class.java)
        assertEquals("0", response.messageStatus)
        assertTrue(response.data!!.isEmpty())
        val missing = gson.fromJson("""{"data":[]}""", HomeFeedResponse::class.java)
        assertNull(missing.messageStatus)
    }
}
