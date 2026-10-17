package com.example.c001apk.compose.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.outlined.AllInclusive
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.ForkRight
import androidx.compose.material.icons.outlined.Source
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.example.c001apk.compose.BuildConfig
import com.example.c001apk.compose.R
import com.example.c001apk.compose.constant.Constants.URL_SOURCE_CODE
import com.example.c001apk.compose.constant.Constants.URL_SOURCE_CODE_FORK
import com.example.c001apk.compose.ui.component.BackButton
import com.example.c001apk.compose.ui.component.settings.BasicListItem
import com.example.c001apk.compose.util.openInBrowser
import com.google.accompanist.drawablepainter.rememberDrawablePainter

/**
 * Created by bggRGjQaUbCoE on 2024/6/2
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    onLicenseClick: () -> Unit,
) {

    val context = LocalContext.current
    val rememberScrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    BackButton { onBackClick() }
                },
                title = { Text(text = "关于") },
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                val drawable = ResourcesCompat.getDrawable(
                    context.resources,
                    R.mipmap.ic_launcher,
                    context.theme
                )
                Image(
                    painter = rememberDrawablePainter(drawable),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            HorizontalDivider()
            BasicListItem(
                leadingImageVector = Icons.Outlined.AllInclusive,
                headlineText = stringResource(id = R.string.app_name),
                supportingText = "维护与新增功能：tiancaiProgramer\n负责本维护分支的后续开发与问题修复。"
            ) { }
            BasicListItem(
                leadingImageVector = Icons.Default.ErrorOutline,
                headlineText = "版本",
                supportingText = "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"
            ) { }
            com.example.c001apk.compose.ui.selfupdate.SelfUpdateSettings()
            BasicListItem(
                leadingImageVector = Icons.Outlined.Code,
                headlineText = "本项目地址 · tiancaiProgramer",
                supportingText = URL_SOURCE_CODE_FORK
            ) {
                context.openInBrowser(URL_SOURCE_CODE_FORK)
            }
            BasicListItem(
                leadingImageVector = Icons.Outlined.ForkRight,
                headlineText = "上游来源：frisk1127",
                supportingText = "https://github.com/frisk1127/c001apk-compose"
            ) {
                context.openInBrowser("https://github.com/frisk1127/c001apk-compose")
            }
            BasicListItem(
                leadingImageVector = Icons.Outlined.ForkRight,
                headlineText = "原项目与原作者：bggRGjQaUbCoE",
                supportingText = URL_SOURCE_CODE
            ) {
                context.openInBrowser(URL_SOURCE_CODE)
            }
            BasicListItem(
                leadingImageVector = Icons.Outlined.Source,
                headlineText = "开源许可证",
                supportingText = "AGPL-3.0；原版权及第三方许可保留，按现状提供。"
            ) {
                onLicenseClick()
            }

        }
    }

}
