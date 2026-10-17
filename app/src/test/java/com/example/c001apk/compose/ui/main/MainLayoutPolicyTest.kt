package com.example.c001apk.compose.ui.main

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import org.junit.Assert.assertEquals
import org.junit.Test

class MainLayoutPolicyTest {
    @Test fun portraitTabletsUseOnePaneEvenWhenWide() {
        assertEquals(WindowWidthSizeClass.Compact, mainLayoutWidthSizeClass(WindowWidthSizeClass.Medium, false))
        assertEquals(WindowWidthSizeClass.Compact, mainLayoutWidthSizeClass(WindowWidthSizeClass.Expanded, false))
    }

    @Test fun landscapeUsesAvailableWindowWidth() {
        assertEquals(WindowWidthSizeClass.Medium, mainLayoutWidthSizeClass(WindowWidthSizeClass.Medium, true))
        assertEquals(WindowWidthSizeClass.Expanded, mainLayoutWidthSizeClass(WindowWidthSizeClass.Expanded, true))
        assertEquals(WindowWidthSizeClass.Compact, mainLayoutWidthSizeClass(WindowWidthSizeClass.Compact, true))
    }

    @Test fun switchingOrientationAndNarrowingTheWindowReevaluatesLayout() {
        val widths = listOf(WindowWidthSizeClass.Expanded, WindowWidthSizeClass.Medium, WindowWidthSizeClass.Compact)
        for (width in widths) {
            assertEquals(WindowWidthSizeClass.Compact, mainLayoutWidthSizeClass(width, false))
            assertEquals(width, mainLayoutWidthSizeClass(width, true))
            assertEquals(WindowWidthSizeClass.Compact, mainLayoutWidthSizeClass(width, false))
        }
    }
}
