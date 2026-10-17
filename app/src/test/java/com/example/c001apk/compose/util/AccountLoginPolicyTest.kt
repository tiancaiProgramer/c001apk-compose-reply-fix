package com.example.c001apk.compose.util

import org.junit.Assert.*
import org.junit.Test

class AccountLoginPolicyTest {
    @Test fun loginAndVerificationRemainOnOfficialHttpsPages() {
        for (url in listOf(AccountLoginPolicy.LOGIN_URL, "https://account.coolapk.com/auth/validate?type=mobile", "https://m.coolapk.com/mp/user/privacy", "https://www.coolapk.com/")) {
            assertTrue(AccountLoginPolicy.allowsNavigation(url))
        }
        for (url in listOf("http://account.coolapk.com/auth/login", "https://account.coolapk.com.example.org/auth/login", "https://example.org/?url=https://account.coolapk.com", "https://account.coolapk.com:8443/auth/login", "https://user@account.coolapk.com/auth/login", "intent://login", "file:///login.html")) {
            assertFalse(AccountLoginPolicy.allowsNavigation(url))
            assertFalse(AccountLoginPolicy.isSuccessLanding(url))
        }
        assertTrue(AccountLoginPolicy.isSuccessLanding("https://www.coolapk.com/"))
        assertFalse(AccountLoginPolicy.isSuccessLanding(AccountLoginPolicy.LOGIN_URL))
    }

    @Test fun cookieNamesAreExactAndEncodedValuesAndEqualsRemainIntact() {
        val credentials = requireNotNull(AccountLoginPolicy.credentials(" notuid=wrong; uid=42; username=%E7%94%A8%E6%88%B7; token=test=token==; other=ignore"))
        assertEquals("42", credentials.uid)
        assertEquals("%E7%94%A8%E6%88%B7", credentials.username)
        assertEquals("test=token==", credentials.token)
        assertFalse(credentials.toString().contains("test=token"))
    }

    @Test fun incompleteMalformedOrOversizedCookiesCannotFinishLogin() {
        for (cookie in listOf(null, "", "notuid=42; username=user; token=token", "uid=42; username=user", "uid=other; username=user; token=token", "uid=42; username=user; token=", "uid=42; username=user; token=has spaces", "uid=42; username=user; token=bad\r\nheader", "uid=42; username=user; token=" + "x".repeat(8193))) {
            assertNull(AccountLoginPolicy.credentials(cookie))
        }
    }

    @Test fun onlyTheConfiguredSafeDeviceIdBecomesAnOfficialDomainCookie() {
        assertEquals("DID=my-configured-id; Domain=.coolapk.com; Path=/; Secure", AccountLoginPolicy.deviceCookie("my-configured-id"))
        for (id in listOf("", "bad; token=other", "bad\r\nheader", "has spaces", "bad,domain", "bad\"quoted", "bad\\escaped", "x".repeat(8193))) {
            assertNull(AccountLoginPolicy.deviceCookie(id))
        }
    }
}
