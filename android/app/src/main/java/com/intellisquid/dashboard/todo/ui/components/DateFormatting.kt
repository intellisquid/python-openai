package com.intellisquid.dashboard.todo.ui.components

import java.text.DateFormat
import java.util.Date

fun formatTimestamp(millis: Long): String =
    DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(millis))
