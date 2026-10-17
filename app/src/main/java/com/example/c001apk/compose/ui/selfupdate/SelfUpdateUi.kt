package com.example.c001apk.compose.ui.selfupdate

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.c001apk.compose.logic.update.GithubReleases
import com.example.c001apk.compose.logic.update.SelfUpdateRepository
import com.example.c001apk.compose.ui.component.settings.BasicListItem
import com.example.c001apk.compose.util.openInBrowser

@Composable
fun StartupUpdateCheck(viewModel: SelfUpdateViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) { viewModel.check(automatic = true) }
    SelfUpdateDialog(viewModel)
}

@Composable
fun SelfUpdateSettings(viewModel: SelfUpdateViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var notifications by remember { mutableStateOf(SelfUpdateRepository.notificationsEnabled(context)) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        notifications = SelfUpdateRepository.notificationsEnabled(context)
    }
    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { allowed ->
        notifications = SelfUpdateRepository.notificationsEnabled(context)
        Toast.makeText(context, if (allowed) "已开启版本更新通知" else "可在系统通知设置中允许 c002apk 通知", Toast.LENGTH_SHORT).show()
    }
    BasicListItem(
        leadingImageVector = Icons.Outlined.SystemUpdate,
        headlineText = if (viewModel.checking) "正在检查 c002apk 更新…" else "检查 c002apk 更新",
        supportingText = "更新来源：tiancaiProgramer · GitHub Releases（含预览版）",
    ) { viewModel.check() }
    BasicListItem(
        leadingImageVector = Icons.Outlined.Notifications,
        headlineText = "后台更新通知",
        supportingText = if (notifications) "已开启 · 联网时约每日检查，新版本提醒一次"
            else "未开启 · 点击允许通知，接收 GitHub 新版本提醒",
    ) {
        val settingsPrefs = context.getSharedPreferences("github_app_updates", 0)
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(context,
                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED &&
                !settingsPrefs.getBoolean("permission_requested", false)) {
            settingsPrefs.edit().putBoolean("permission_requested", true).apply()
            permission.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            val settings = if (Build.VERSION.SDK_INT >= 26) {
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            } else Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))
            context.startActivity(settings)
        }
    }
    SelfUpdateDialog(viewModel)
}

@Composable
private fun SelfUpdateDialog(viewModel: SelfUpdateViewModel) {
    val context = LocalContext.current
    val release = viewModel.release
    val message = viewModel.message
    if (release != null) {
        AlertDialog(
            onDismissRequest = viewModel::dismiss,
            title = { Text("发现新版本 ${release.tag}") },
            text = { Column(Modifier.heightIn(max = 360.dp).verticalScroll(rememberScrollState())) {
                val notes = remember(release.notes) {
                    release.notes.replace(Regex("\\[([^\\]]+)]\\([^)]*\\)"), "$1")
                        .replace("**", "").replace("`", "")
                        .replace(Regex("(?m)^#{1,6}\\s+"), "")
                }
                Text(notes.ifBlank { "新版本已发布，点击下载更新。" })
                TextButton(onClick = { context.openInBrowser(release.pageUrl) }) { Text("查看完整发布说明") }
            } },
            confirmButton = { TextButton(onClick = {
                context.openInBrowser(release.apkUrl)
                viewModel.dismiss()
            }) { Text("下载更新") } },
            dismissButton = { TextButton(onClick = viewModel::dismiss) { Text("稍后") } },
        )
    } else if (message != null) {
        AlertDialog(
            onDismissRequest = viewModel::dismiss,
            title = { Text("c002apk 更新") },
            text = { Text(message) },
            confirmButton = { TextButton(onClick = viewModel::dismiss) { Text("确定") } },
            dismissButton = { TextButton(onClick = { context.openInBrowser(GithubReleases.PAGE_URL) }) { Text("发布页") } },
        )
    }
}
