package com.intellisquid.dashboard.todo.ui.components

import com.intellisquid.dashboard.todo.data.Priority
import com.intellisquid.dashboard.todo.data.TodoItem
import com.intellisquid.dashboard.todo.data.TodoStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class SortableTableTest {

    private fun item(title: String, priority: Priority, createdAt: Long) = TodoItem(
        id = createdAt,
        title = title,
        priority = priority,
        status = TodoStatus.ACTIVE,
        createdAt = createdAt,
        updatedAt = createdAt,
    )

    private val items = listOf(
        item("Banana", Priority.NORMAL, createdAt = 200L),
        item("apple", Priority.HIGH, createdAt = 100L),
        item("Cherry", Priority.LOW, createdAt = 300L),
    )

    @Test
    fun sortByTitle_isCaseInsensitiveAscending() {
        val sorted = sortTodos(items, SortState(TodoColumn.TITLE, SortDirection.ASCENDING))
        assertEquals(listOf("apple", "Banana", "Cherry"), sorted.map { it.title })
    }

    @Test
    fun sortByTitle_descendingReversesOrder() {
        val sorted = sortTodos(items, SortState(TodoColumn.TITLE, SortDirection.DESCENDING))
        assertEquals(listOf("Cherry", "Banana", "apple"), sorted.map { it.title })
    }

    @Test
    fun sortByCreated_ascendingIsOldestFirst() {
        val sorted = sortTodos(items, SortState(TodoColumn.CREATED, SortDirection.ASCENDING))
        assertEquals(listOf(100L, 200L, 300L), sorted.map { it.createdAt })
    }

    @Test
    fun sortByCreated_descendingIsNewestFirst() {
        val sorted = sortTodos(items, SortState(TodoColumn.CREATED, SortDirection.DESCENDING))
        assertEquals(listOf(300L, 200L, 100L), sorted.map { it.createdAt })
    }

    @Test
    fun sortByPriority_ascendingIsLowToHigh() {
        val sorted = sortTodos(items, SortState(TodoColumn.PRIORITY, SortDirection.ASCENDING))
        assertEquals(listOf(Priority.LOW, Priority.NORMAL, Priority.HIGH), sorted.map { it.priority })
    }

    @Test
    fun sortByPriority_descendingIsHighToLow() {
        val sorted = sortTodos(items, SortState(TodoColumn.PRIORITY, SortDirection.DESCENDING))
        assertEquals(listOf(Priority.HIGH, Priority.NORMAL, Priority.LOW), sorted.map { it.priority })
    }

    @Test
    fun toggleSort_sameColumn_flipsDirection() {
        val ascending = SortState(TodoColumn.CREATED, SortDirection.ASCENDING)
        assertEquals(SortDirection.DESCENDING, toggleSort(ascending, TodoColumn.CREATED).direction)
        val descending = SortState(TodoColumn.CREATED, SortDirection.DESCENDING)
        assertEquals(SortDirection.ASCENDING, toggleSort(descending, TodoColumn.CREATED).direction)
    }

    @Test
    fun toggleSort_differentColumn_switchesAndResetsToAscending() {
        val current = SortState(TodoColumn.CREATED, SortDirection.DESCENDING)
        val result = toggleSort(current, TodoColumn.PRIORITY)
        assertEquals(TodoColumn.PRIORITY, result.column)
        assertEquals(SortDirection.ASCENDING, result.direction)
    }

    @Test
    fun sortStateSaver_roundTripsThroughSaveRestore() {
        val original = SortState(TodoColumn.PRIORITY, SortDirection.DESCENDING)
        val saved = SortStateSaver.save(original)!!
        val restored = SortStateSaver.restore(saved)
        assertEquals(original, restored)
    }
}
