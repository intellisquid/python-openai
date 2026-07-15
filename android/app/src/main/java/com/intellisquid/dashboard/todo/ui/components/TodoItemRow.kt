package com.intellisquid.dashboard.todo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.intellisquid.dashboard.todo.data.Priority
import com.intellisquid.dashboard.todo.data.TodoItem
import com.intellisquid.dashboard.todo.data.TodoStatus

@Composable
fun ActiveTodoRow(
    item: TodoItem,
    onToggleCompleted: (Boolean) -> Unit,
    onArchive: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val completed = item.status == TodoStatus.COMPLETED
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = completed, onCheckedChange = onToggleCompleted)
            Column(modifier = Modifier.weight(1f).padding(start = 4.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (completed) TextDecoration.LineThrough else null,
                )
                if (item.notes.isNotBlank()) {
                    Text(text = item.notes, style = MaterialTheme.typography.bodySmall)
                }
                Text(text = priorityLabel(item.priority), style = MaterialTheme.typography.labelSmall)
            }
            IconButton(onClick = onArchive) {
                Icon(Icons.Filled.Archive, contentDescription = "Archive")
            }
        }
    }
}

@Composable
fun ArchivedTodoRow(
    item: TodoItem,
    onRestore: () -> Unit,
    onDeleteForever: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.title, style = MaterialTheme.typography.bodyLarge)
                item.archivedAt?.let {
                    Text(text = "Archived ${formatTimestamp(it)}", style = MaterialTheme.typography.labelSmall)
                }
            }
            IconButton(onClick = onRestore) {
                Icon(Icons.Filled.Restore, contentDescription = "Restore")
            }
            IconButton(onClick = onDeleteForever) {
                Icon(Icons.Filled.DeleteForever, contentDescription = "Delete forever")
            }
        }
    }
}

private fun priorityLabel(priority: Priority): String = when (priority) {
    Priority.LOW -> "Low priority"
    Priority.NORMAL -> "Normal priority"
    Priority.HIGH -> "High priority"
}
