package com.intellisquid.dashboard.todo.backup

import com.intellisquid.dashboard.todo.data.Priority
import com.intellisquid.dashboard.todo.data.TodoItem
import com.intellisquid.dashboard.todo.data.TodoStatus
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class BackupModelsTest {

    private val json = Json { prettyPrint = true; encodeDefaults = true }

    @Test
    fun todoItem_survivesEntryRoundTrip() {
        val original = TodoItem(
            id = 42L,
            title = "Ship the prototype",
            notes = "Dashboard + archive + backup",
            priority = Priority.HIGH,
            status = TodoStatus.ARCHIVED,
            createdAt = 1_000L,
            updatedAt = 2_000L,
            completedAt = 1_500L,
            archivedAt = 2_000L,
            dueAt = 3_000L,
        )

        val restored = original.toBackupEntry().toTodoItem()

        // id is intentionally dropped by the backup format: restores always insert as new rows.
        assertEquals(original.copy(id = 0L), restored)
    }

    @Test
    fun backupPayload_survivesJsonRoundTrip() {
        val payload = BackupPayload(
            exportedAt = 123L,
            items = listOf(
                TodoItem(
                    title = "Buy milk",
                    createdAt = 1L,
                    updatedAt = 1L,
                ).toBackupEntry(),
            ),
        )

        val encoded = json.encodeToString(BackupPayload.serializer(), payload)
        val decoded = json.decodeFromString(BackupPayload.serializer(), encoded)

        assertEquals(payload, decoded)
    }

    @Test
    fun unknownStatusOrPriority_fallsBackToSafeDefaultsInsteadOfCrashing() {
        val entry = TodoBackupEntry(
            title = "From a future app version",
            notes = "",
            priority = "URGENT_NEW_LEVEL",
            status = "SNOOZED",
            createdAt = 1L,
            updatedAt = 1L,
            completedAt = null,
            archivedAt = null,
            dueAt = null,
        )

        val item = entry.toTodoItem()

        assertEquals(Priority.NORMAL, item.priority)
        assertEquals(TodoStatus.ACTIVE, item.status)
    }
}
