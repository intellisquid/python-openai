package com.intellisquid.dashboard.todo.ui.theme

import androidx.compose.ui.graphics.Color

val SeaGreen80 = Color(0xFFA8D8C8)
val SeaGreenGrey80 = Color(0xFFC2D0CB)
val Teal80 = Color(0xFF9ECFD6)

val SeaGreen40 = Color(0xFF2E6F5E)
val SeaGreenGrey40 = Color(0xFF4B635A)
val Teal40 = Color(0xFF3A6B72)

// Fixed dashboard-table row stripe colors — intentionally NOT theme-adaptive (used regardless of
// light/dark mode). Both are near-black (~15-17:1 contrast against white text, past WCAG AAA's
// 7:1), so white cell text stays legible even on a dim or glare-washed phone screen.
val TableRowMidnightGreen = Color(0xFF10261E)
val TableRowLowKeyFuchsia = Color(0xFF2A1228)
