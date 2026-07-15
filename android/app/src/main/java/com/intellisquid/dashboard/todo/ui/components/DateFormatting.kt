package com.intellisquid.dashboard.todo.ui.components

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(millis: Long): String =
    DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(millis))

// Fixed literal format (not locale-adaptive like formatTimestamp above) for the table's
// monospaced "Created" column, so digit widths stay constant across locales.
private val compactDateTimeFormat = SimpleDateFormat("HH:mm / MM.dd.yy", Locale.US)

fun formatTimestampCompact(millis: Long): String = compactDateTimeFormat.format(Date(millis))
