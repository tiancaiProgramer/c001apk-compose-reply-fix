package com.example.c001apk.compose

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.c001apk.compose.logic.datastore.UserPreferencesCompat
import com.example.c001apk.compose.logic.providable.LocalUserPreferences
import com.example.c001apk.compose.ui.login.LoginScreen
import com.example.c001apk.compose.ui.main.MainActivity
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/** Read-only checks on real public login pages. Never enters a phone, sends SMS, or solves challenges. */
@RunWith(AndroidJUnit4::class)
class LoginFlowDeviceTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()

    private fun findWebView(view: View): WebView? {
        if (view is WebView) return view
        if (view is ViewGroup) for (index in 0 until view.childCount) {
            findWebView(view.getChildAt(index))?.let { return it }
        }
        return null
    }

    private fun js(scenario: ActivityScenario<MainActivity>, expression: String): String? {
        val result = AtomicReference<String?>()
        val done = CountDownLatch(1)
        scenario.onActivity { activity ->
            val view = findWebView(activity.findViewById(android.R.id.content))
            if (view == null) done.countDown()
            else view.evaluateJavascript(expression) { result.set(it); done.countDown() }
        }
        assertTrue("WebView callback timeout", done.await(10, TimeUnit.SECONDS))
        return result.get()
    }

    private fun awaitPage(scenario: ActivityScenario<MainActivity>, expression: String) {
        val deadline = SystemClock.elapsedRealtime() + 45000
        while (SystemClock.elapsedRealtime() < deadline) {
            if (js(scenario, expression) == "true") return
            SystemClock.sleep(300)
        }
        fail("Official login page did not reach the expected state")
    }

    @Test fun officialPhoneAndPasswordLoginStayInAppAndSurviveRecreation() {
        assertTrue("Use an isolated package; do not test against a signed-in app", instrumentation.targetContext.packageName.endsWith(".logintest"))
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            val repository = AtomicReference<com.example.c001apk.compose.logic.repository.UserPreferencesRepository>()
            scenario.onActivity { repository.set(it.userPreferencesRepository) }
            assertFalse("Manual login already completed; preserve it and skip this logged-out page test",
                runBlocking { repository.get().data.first().isLogin })
            fun showLogin() {
                scenario.onActivity { activity ->
                    activity.setContent {
                        val prefs by activity.userPreferencesRepository.data.collectAsState(UserPreferencesCompat.default())
                        CompositionLocalProvider(LocalUserPreferences provides prefs) {
                            MaterialTheme { LoginScreen(onBackClick = {}) }
                        }
                    }
                }
            }
            showLogin()
            awaitPage(scenario, "!!document.getElementById('mobileInput') && typeof initNECaptcha==='function' && typeof createNEGuardian==='function'")
            assertEquals("true", js(scenario, "location.host==='account.coolapk.com' && !!document.getElementById('button-sms')"))
            js(scenario, "location.href='https://account.coolapk.com/auth/loginByCoolApk'; true")
            awaitPage(scenario, "!!document.getElementById('passwordInput') && !!document.getElementById('codeInput') && typeof initNECaptcha==='function'")
            js(scenario, "location.href='intent://untrusted-login'; true")
            assertEquals("true", js(scenario, "location.host==='account.coolapk.com' && !!document.getElementById('passwordInput')"))
            // Real activity recreation must preserve the current official verification step.
            scenario.recreate()
            showLogin()
            awaitPage(scenario, "location.pathname==='/auth/loginByCoolApk' && !!document.getElementById('passwordInput')")
        }
    }
}
