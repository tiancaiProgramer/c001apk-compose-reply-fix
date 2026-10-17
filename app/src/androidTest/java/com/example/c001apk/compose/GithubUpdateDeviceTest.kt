package com.example.c001apk.compose

import android.app.NotificationManager
import android.app.job.JobScheduler
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.c001apk.compose.logic.update.SelfUpdateRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit
import java.io.FileInputStream
import android.os.SystemClock

/** Run only in a disposable package with an older version to exercise real published updates. */
@RunWith(AndroidJUnit4::class)
class GithubUpdateDeviceTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext

    @Test fun publishedGithubUpdateNotifiesOnceAndOpensTheRepositoryRelease() = runBlocking {
        assertTrue("Use the isolated update test package", context.packageName.endsWith(".updatetest"))
        val prefs = context.getSharedPreferences("github_app_updates", 0)
        prefs.edit().clear().commit()
        if (Build.VERSION.SDK_INT >= 33) {
            instrumentation.uiAutomation.executeShellCommand("pm grant ${context.packageName} android.permission.POST_NOTIFICATIONS").use { descriptor ->
                FileInputStream(descriptor.fileDescriptor).use { it.readBytes() }
            }
        }
        SelfUpdateRepository.schedule(context)
        assertTrue(SelfUpdateRepository.notificationsEnabled(context))
        val release = requireNotNull(SelfUpdateRepository.check(context)) {
            "Expected published update for ${BuildConfig.VERSION_NAME}"
        }
        assertTrue(release.apkUrl.startsWith("https://github.com/tiancaiProgramer/c002apk/releases/download/"))
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.cancelAll()
        SelfUpdateRepository.notifyIfNew(context, release)
        val deadline = SystemClock.elapsedRealtime() + 5000
        while (manager.activeNotifications.isEmpty() && SystemClock.elapsedRealtime() < deadline) SystemClock.sleep(50)
        val first = manager.activeNotifications.single()
        assertTrue(first.notification.extras.getString("android.title").orEmpty().contains(release.tag))
        assertNotNull(first.notification.contentIntent)
        assertEquals(release.tag, prefs.getString("notified_tag", null))
        manager.cancel(first.id)
        while (manager.activeNotifications.isNotEmpty() && SystemClock.elapsedRealtime() < deadline) SystemClock.sleep(50)
        SelfUpdateRepository.notifyIfNew(context, release)
        assertTrue("A dismissed version must not notify again", manager.activeNotifications.isEmpty())
    }

    @Test fun backgroundCheckIsPersistedDailyAndRequiresNetwork() {
        assertTrue("Use the isolated update test package", context.packageName.endsWith(".updatetest"))
        SelfUpdateRepository.schedule(context)
        val job = requireNotNull(context.getSystemService(JobScheduler::class.java).getPendingJob(SelfUpdateRepository.JOB_ID))
        assertTrue(job.isPersisted)
        assertTrue(job.isPeriodic)
        assertEquals(TimeUnit.HOURS.toMillis(24), job.intervalMillis)
        assertEquals(android.app.job.JobInfo.NETWORK_TYPE_ANY, job.networkType)
    }
}
