package com.intellisquid.dashboard.todo.backup

import android.content.ContentResolver
import android.net.Uri
import com.intellisquid.dashboard.todo.data.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.IOException

private val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

sealed interface RestoreResult {
    data class Success(val items: List<TodoItem>) : RestoreResult
    data class Failure(val message: String) : RestoreResult
}

/**
 * Reads/writes the full to-do archive as a single human-readable JSON file through a
 * Storage Access Framework [Uri], so backups can live anywhere the user picks
 * (device storage, SD card, a synced Drive folder, etc.) independent of the app's lifetime.
 */
class BackupManager(private val contentResolver: ContentResolver) {

    suspend fun exportTo(uri: Uri, items: List<TodoItem>) = withContext(Dispatchers.IO) {
        val payload = BackupPayload(
            exportedAt = System.currentTimeMillis(),
            items = items.map { it.toBackupEntry() },
        )
        val bytes = json.encodeToString(BackupPayload.serializer(), payload).toByteArray(Charsets.UTF_8)
        contentResolver.openOutputStream(uri, "wt")?.use { it.write(bytes) }
            ?: throw IOException("Unable to open $uri for writing")
    }

    suspend fun importFrom(uri: Uri): RestoreResult = withContext(Dispatchers.IO) {
        try {
            val text = contentResolver.openInputStream(uri)?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }
                ?: return@withContext RestoreResult.Failure("Unable to open the selected file")
            val payload = json.decodeFromString(BackupPayload.serializer(), text)
            RestoreResult.Success(payload.items.map { it.toTodoItem() })
        } catch (e: Exception) {
            RestoreResult.Failure(e.message ?: "The selected file is not a valid backup")
        }
    }

    companion object {
        const val MIME_TYPE = "application/json"

        fun suggestedFileName(clock: Long = System.currentTimeMillis()): String =
            "dashboard-todo-backup-$clock.json"
    }
}
