package com.intellisquid.dashboard.todo.data

import kotlinx.coroutines.flow.Flow

/**
 * Archiving is the only path items take out of the dashboard. Items are never deleted by
 * ordinary use of the app — [purge] exists solely for the archive screen's explicit,
 * user-confirmed "delete forever" action.
 */
class TodoRepository(private val dao: TodoDao) {

    fun observeDashboard(): Flow<List<TodoItem>> = dao.observeDashboard()

    fun observeArchived(): Flow<List<TodoItem>> = dao.observeArchived()

    suspend fun addTodo(title: String, notes: String = "", priority: Priority = Priority.NORMAL, dueAt: Long? = null) {
        val now = System.currentTimeMillis()
        dao.insert(
            TodoItem(
                title = title,
                notes = notes,
                priority = priority,
                status = TodoStatus.ACTIVE,
                createdAt = now,
                updatedAt = now,
                dueAt = dueAt,
            ),
        )
    }

    suspend fun setCompleted(item: TodoItem, completed: Boolean) {
        val now = System.currentTimeMillis()
        dao.update(
            item.copy(
                status = if (completed) TodoStatus.COMPLETED else TodoStatus.ACTIVE,
                completedAt = if (completed) now else null,
                updatedAt = now,
            ),
        )
    }

    suspend fun archive(item: TodoItem) {
        val now = System.currentTimeMillis()
        dao.update(item.copy(status = TodoStatus.ARCHIVED, archivedAt = now, updatedAt = now))
    }

    suspend fun restoreFromArchive(item: TodoItem) {
        val now = System.currentTimeMillis()
        dao.update(item.copy(status = TodoStatus.ACTIVE, archivedAt = null, updatedAt = now))
    }

    /** Permanently and irreversibly deletes an archived item. Only callable from the archive screen. */
    suspend fun purge(item: TodoItem) {
        dao.delete(item)
    }

    suspend fun update(item: TodoItem) {
        dao.update(item.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun getAllForBackup(): List<TodoItem> = dao.getAllForBackup()

    /** Restores items from a backup as brand-new rows, so a restore never clobbers existing data. */
    suspend fun importItems(items: List<TodoItem>) {
        dao.insertAll(items.map { it.copy(id = 0L) })
    }
}
