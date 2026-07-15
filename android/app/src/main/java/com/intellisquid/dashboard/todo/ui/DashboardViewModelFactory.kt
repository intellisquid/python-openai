package com.intellisquid.dashboard.todo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.intellisquid.dashboard.todo.backup.BackupManager
import com.intellisquid.dashboard.todo.data.TodoRepository

class DashboardViewModelFactory(
    private val repository: TodoRepository,
    private val backupManager: BackupManager,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(DashboardViewModel::class.java))
        return DashboardViewModel(repository, backupManager) as T
    }
}
