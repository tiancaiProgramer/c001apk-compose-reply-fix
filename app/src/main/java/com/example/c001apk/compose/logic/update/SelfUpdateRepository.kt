package com.example.c001apk.compose.logic.update

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.c001apk.compose.BuildConfig
import com.example.c001apk.compose.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

/** This client is deliberately independent of Coolapk's cookies and device headers. */
object SelfUpdateRepository {
    const val JOB_ID = 200217
    const val CHANNEL = "github_app_updates"
    private const val NOTIFICATION_ID = 200218
    private val client by lazy {
        OkHttpClient.Builder().callTimeout(20, TimeUnit.SECONDS).build()
    }
    private fun preferences(context: Context) = context.getSharedPreferences("github_app_updates", Context.MODE_PRIVATE)

    suspend fun check(context: Context, automatic: Boolean = false): GithubRelease? = withContext(Dispatchers.IO) {
        // Recover the periodic job if the system removed it during an app stop or upgrade.
        schedule(context)
        val prefs = preferences(context)
        val now = System.currentTimeMillis()
        val elapsed = now - prefs.getLong("last_foreground_check", 0)
        if (automatic && elapsed in 0 until TimeUnit.HOURS.toMillis(6)) return@withContext null
        val request = Request.Builder().url(GithubReleases.API_URL)
            .header("Accept", "application/vnd.github+json")
            .header("User-Agent", "c002apk/${BuildConfig.VERSION_NAME}").build()
        val result = client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("GitHub HTTP ${response.code}")
            val body = response.body ?: throw IOException("Empty GitHub response")
            // Bound the untrusted release metadata, including unbounded release descriptions.
            val source = body.source()
            source.request(2L * 1024 * 1024 + 1)
            if (source.buffer.size > 2L * 1024 * 1024) throw IOException("GitHub response too large")
            GithubReleases.newerRelease(source.readUtf8(), BuildConfig.VERSION_NAME)
        }
        if (automatic) prefs.edit().putLong("last_foreground_check", now).apply()
        result
    }

    fun schedule(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel(CHANNEL, "c002apk 版本更新", NotificationManager.IMPORTANCE_DEFAULT)
            )
        }
        val scheduler = context.getSystemService(JobScheduler::class.java)
        if (scheduler.getPendingJob(JOB_ID) == null) {
            scheduler.schedule(JobInfo.Builder(JOB_ID, ComponentName(context, ReleaseUpdateJobService::class.java))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(TimeUnit.HOURS.toMillis(24))
                .setPersisted(true).build())
        }
    }

    fun notificationsEnabled(context: Context): Boolean {
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return false
        return Build.VERSION.SDK_INT < 26 || context.getSystemService(NotificationManager::class.java)
            .getNotificationChannel(CHANNEL)?.importance != NotificationManager.IMPORTANCE_NONE
    }

    @Suppress("MissingPermission") // The runtime notification permission is checked before posting.
    fun notifyIfNew(context: Context, release: GithubRelease) {
        val prefs = preferences(context)
        if (!notificationsEnabled(context) || prefs.getString("notified_tag", null) == release.tag) return
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(release.pageUrl))
        val pending = PendingIntent.getActivity(context, NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, CHANNEL)
            .setSmallIcon(R.drawable.ic_stat_update)
            .setContentTitle("c002apk 有新版本：${release.tag}")
            .setContentText("点击查看更新说明并下载")
            .setContentIntent(pending).setAutoCancel(true).build()
        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
            prefs.edit().putString("notified_tag", release.tag).apply()
        } catch (_: SecurityException) { /* Permission may have changed while fetching. */ }
    }
}
