package com.example.c001apk.compose.ui.main

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

/** Portrait uses one content pane, including tablets wider than the compact threshold. */
internal fun mainLayoutWidthSizeClass(
    windowWidthSizeClass: WindowWidthSizeClass,
    isLandscape: Boolean,
): WindowWidthSizeClass = if (isLandscape) windowWidthSizeClass else WindowWidthSizeClass.Compact
