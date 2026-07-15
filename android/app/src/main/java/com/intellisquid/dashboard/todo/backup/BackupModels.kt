package com.intellisquid.dashboard.todo.backup

import com.intellisquid.dashboard.todo.data.Priority
import com.intellisquid.dashboard.todo.data.TodoItem
import com.intellisquid.dashboard.todo.data.TodoStatus
import kotlinx.serialization.Serializable

/** File format version. Bump whenever a field is added/removed/renamed so old backups still import cleanly. */
const val BACKUP_FORMAT_VERSION = 1

@Serializable
data class TodoBackupEntry(
    val title: String,
    val notes: String,
    val priority: String,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long,
    val completedAt: Long?,
    val archivedAt: Long?,
    val dueAt: Long?,
)

@Serializable
data class BackupPayload(
    val formatVersion: Int = BACKUP_FORMAT_VERSION,
    val exportedAt: Long,
    val items: List<TodoBackupEntry>,
)

fun TodoItem.toBackupEntry() = TodoBackupEntry(
    title = title,
    notes = notes,
    priority = priority.name,
    status = status.name,
    createdAt = createdAt,
    updatedAt = updatedAt,
    completedAt = completedAt,
    archivedAt = archivedAt,
    dueAt = dueAt,
)

fun TodoBackupEntry.toTodoItem() = TodoItem(
    title = title,
    notes = notes,
    priority = runCatching { Priority.valueOf(priority) }.getOrDefault(Priority.NORMAL),
    status = runCatching { TodoStatus.valueOf(status) }.getOrDefault(TodoStatus.ACTIVE),
    createdAt = createdAt,
    updatedAt = updatedAt,
    completedAt = completedAt,
    archivedAt = archivedAt,
    dueAt = dueAt,
)
