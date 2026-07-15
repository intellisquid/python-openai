package com.intellisquid.dashboard.todo.data

/**
 * Lifecycle of a [TodoItem]. Archiving never deletes data — only [PURGED] items
 * (an explicit, user-confirmed action from the archive screen) are removed from the database.
 */
enum class TodoStatus {
    ACTIVE,
    COMPLETED,
    ARCHIVED,
}
