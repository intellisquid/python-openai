package com.intellisquid.dashboard.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Priority {
    LOW,
    NORMAL,
    HIGH,
}

@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val notes: String = "",
    val priority: Priority = Priority.NORMAL,
    val status: TodoStatus = TodoStatus.ACTIVE,
    val createdAt: Long,
    val updatedAt: Long,
    val completedAt: Long? = null,
    val archivedAt: Long? = null,
    val dueAt: Long? = null,
)
