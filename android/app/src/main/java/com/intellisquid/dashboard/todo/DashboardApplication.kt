package com.intellisquid.dashboard.todo

import android.app.Application
import com.intellisquid.dashboard.todo.backup.BackupManager
import com.intellisquid.dashboard.todo.data.TodoDatabase
import com.intellisquid.dashboard.todo.data.TodoRepository

class DashboardApplication : Application() {

    val repository: TodoRepository by lazy {
        TodoRepository(TodoDatabase.getInstance(this).todoDao())
    }

    val backupManager: BackupManager by lazy {
        BackupManager(contentResolver)
    }
}
