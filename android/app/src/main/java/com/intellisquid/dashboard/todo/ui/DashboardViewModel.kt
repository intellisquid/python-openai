package com.intellisquid.dashboard.todo.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intellisquid.dashboard.todo.backup.BackupManager
import com.intellisquid.dashboard.todo.backup.RestoreResult
import com.intellisquid.dashboard.todo.data.Priority
import com.intellisquid.dashboard.todo.data.TodoItem
import com.intellisquid.dashboard.todo.data.TodoRepository
import com.intellisquid.dashboard.todo.data.TodoStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: TodoRepository,
    private val backupManager: BackupManager,
) : ViewModel() {

    val dashboardItems: StateFlow<List<TodoItem>> = repository.observeDashboard()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val archivedItems: StateFlow<List<TodoItem>> = repository.observeArchived()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun addTodo(title: String, notes: String, priority: Priority) {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch { repository.addTodo(trimmed, notes.trim(), priority) }
    }

    fun setCompleted(item: TodoItem, completed: Boolean) {
        viewModelScope.launch { repository.setCompleted(item, completed) }
    }

    fun archive(item: TodoItem) {
        viewModelScope.launch {
            repository.archive(item)
            _message.value = "Archived \"${item.title}\""
        }
    }

    fun restoreFromArchive(item: TodoItem) {
        viewModelScope.launch {
            repository.restoreFromArchive(item)
            _message.value = "Restored \"${item.title}\" to the dashboard"
        }
    }

    fun purge(item: TodoItem) {
        viewModelScope.launch {
            repository.purge(item)
            _message.value = "Permanently deleted \"${item.title}\""
        }
    }

    fun exportBackup(uri: Uri) {
        viewModelScope.launch {
            runCatching {
                val items = repository.getAllForBackup()
                backupManager.exportTo(uri, items)
            }.onSuccess {
                _message.value = "Backup saved"
            }.onFailure { e ->
                _message.value = "Backup failed: ${e.message}"
            }
        }
    }

    fun importBackup(uri: Uri) {
        viewModelScope.launch {
            when (val result = backupManager.importFrom(uri)) {
                is RestoreResult.Success -> {
                    repository.importItems(result.items)
                    _message.value = "Restored ${result.items.size} item(s) from backup"
                }
                is RestoreResult.Failure -> {
                    _message.value = "Restore failed: ${result.message}"
                }
            }
        }
    }

    fun consumeMessage() {
        _message.value = null
    }

    companion object {
        fun activeCount(items: List<TodoItem>) = items.count { it.status == TodoStatus.ACTIVE }
    }
}
