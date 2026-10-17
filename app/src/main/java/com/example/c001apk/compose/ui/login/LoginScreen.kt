package com.example.c001apk.compose.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.c001apk.compose.logic.providable.LocalUserPreferences
import com.example.c001apk.compose.ui.component.BackButton

/**
 * Maintained by tiancaiProgramer.
 * Created by bggRGjQaUbCoE on 2024/6/10
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onBackClick: () -> Unit) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val prefs = LocalUserPreferences.current
    var reload by remember { mutableIntStateOf(0) }
    var progress by remember { mutableIntStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(prefs.isLogin) {
        if (prefs.isLogin) onBackClick()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Start + WindowInsetsSides.Top),
                navigationIcon = { BackButton { onBackClick() } },
                title = { Text("登录") },
                actions = {
                    TextButton(enabled = !viewModel.saving, onClick = { reload++ }) {
                        Text("重新加载")
                    }
                },
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).imePadding()) {
            Text("图片验证和短信验证均在此完成，登录后自动返回。",
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodySmall)
            if (viewModel.saving) LinearProgressIndicator(Modifier.fillMaxWidth())
            else if (progress < 100) LinearProgressIndicator(progress = { progress / 100f }, modifier = Modifier.fillMaxWidth())
            (viewModel.error ?: error)?.let {
                Text(it, Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = MaterialTheme.colorScheme.error)
            }
            AccountLoginWebView(
                userAgent = prefs.userAgent,
                deviceId = prefs.szlmId,
                reload = reload,
                onCredentials = viewModel::finishLogin,
                onProgress = { progress = it },
                onError = { error = it },
                onBack = onBackClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
