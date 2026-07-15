package com.intellisquid.dashboard.todo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.intellisquid.dashboard.todo.data.TodoItem

enum class TodoColumn { TITLE, CREATED, PRIORITY }

enum class SortDirection {
    ASCENDING,
    DESCENDING,
    ;

    fun flipped(): SortDirection = if (this == ASCENDING) DESCENDING else ASCENDING
}

data class SortState(val column: TodoColumn, val direction: SortDirection)

val SortStateSaver: Saver<SortState, List<String>> = Saver(
    save = { listOf(it.column.name, it.direction.name) },
    restore = { SortState(TodoColumn.valueOf(it[0]), SortDirection.valueOf(it[1])) },
)

fun sortTodos(items: List<TodoItem>, sortState: SortState): List<TodoItem> {
    val comparator: Comparator<TodoItem> = when (sortState.column) {
        TodoColumn.TITLE -> compareBy(String.CASE_INSENSITIVE_ORDER) { it.title }
        TodoColumn.CREATED -> compareBy { it.createdAt }
        // Priority is declared LOW, NORMAL, HIGH — ordinal order already doubles as severity
        // order, same convention data/Converters.kt relies on for DB sort order.
        TodoColumn.PRIORITY -> compareBy { it.priority.ordinal }
    }
    return items.sortedWith(if (sortState.direction == SortDirection.DESCENDING) comparator.reversed() else comparator)
}

fun toggleSort(current: SortState, clicked: TodoColumn): SortState =
    if (current.column == clicked) {
        current.copy(direction = current.direction.flipped())
    } else {
        SortState(clicked, SortDirection.ASCENDING)
    }

@Composable
fun TodoTableHeader(sortState: SortState, onSort: (TodoColumn) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)) {
        SortableHeaderCell("Title", TodoColumn.TITLE, sortState, onSort, modifier = Modifier.weight(2f))
        SortableHeaderCell("Created", TodoColumn.CREATED, sortState, onSort, modifier = Modifier.weight(1.6f))
        SortableHeaderCell("Priority", TodoColumn.PRIORITY, sortState, onSort, modifier = Modifier.weight(1.1f))
        Spacer(modifier = Modifier.weight(1.3f)) // Actions column: fixed, not sortable
    }
}

@Composable
private fun SortableHeaderCell(
    label: String,
    column: TodoColumn,
    sortState: SortState,
    onSort: (TodoColumn) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.clickable { onSort(column) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        if (sortState.column == column) {
            Icon(
                imageVector = if (sortState.direction == SortDirection.ASCENDING) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                contentDescription = if (sortState.direction == SortDirection.ASCENDING) "Sorted ascending" else "Sorted descending",
                modifier = Modifier.size(16.dp),
            )
        }
    }
}
