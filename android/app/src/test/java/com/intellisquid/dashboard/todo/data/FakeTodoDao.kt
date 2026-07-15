package com.intellisquid.dashboard.todo.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** In-memory stand-in for Room so repository logic can be unit tested on the plain JVM. */
class FakeTodoDao : TodoDao {

    private val all = MutableStateFlow<List<TodoItem>>(emptyList())
    private var nextId = 1L

    override fun observeDashboard(): Flow<List<TodoItem>> =
        all.map { list -> list.filter { it.status == TodoStatus.ACTIVE || it.status == TodoStatus.COMPLETED } }

    override fun observeArchived(): Flow<List<TodoItem>> =
        all.map { list -> list.filter { it.status == TodoStatus.ARCHIVED } }

    override suspend fun getAllForBackup(): List<TodoItem> = all.value

    override suspend fun insert(item: TodoItem): Long {
        val assigned = item.copy(id = nextId++)
        all.value = all.value + assigned
        return assigned.id
    }

    override suspend fun insertAll(items: List<TodoItem>): List<Long> = items.map { insert(it) }

    override suspend fun update(item: TodoItem) {
        all.value = all.value.map { if (it.id == item.id) item else it }
    }

    override suspend fun delete(item: TodoItem) {
        all.value = all.value.filterNot { it.id == item.id }
    }

    override suspend fun getById(id: Long): TodoItem? = all.value.firstOrNull { it.id == id }
}
