package com.intellisquid.dashboard.todo.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStatus(status: TodoStatus): String = status.name

    @TypeConverter
    fun toStatus(value: String): TodoStatus = TodoStatus.valueOf(value)

    // Stored as ordinal (not name) so "ORDER BY priority" sorts by actual severity —
    // Priority is declared LOW, NORMAL, HIGH, so higher ordinal means higher priority.
    @TypeConverter
    fun fromPriority(priority: Priority): Int = priority.ordinal

    @TypeConverter
    fun toPriority(value: Int): Priority = Priority.entries[value]
}
