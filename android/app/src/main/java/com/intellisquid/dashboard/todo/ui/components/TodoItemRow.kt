package com.intellisquid.dashboard.todo.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.intellisquid.dashboard.todo.data.Priority
import com.intellisquid.dashboard.todo.data.TodoItem
import com.intellisquid.dashboard.todo.data.TodoStatus
import com.intellisquid.dashboard.todo.ui.theme.TableRowLowKeyFuchsia
import com.intellisquid.dashboard.todo.ui.theme.TableRowMidnightGreen

@Composable
fun ActiveTodoTableRow(
    item: TodoItem,
    isEvenRow: Boolean,
    onToggleCompleted: (Boolean) -> Unit,
    onArchive: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val completed = item.status == TodoStatus.COMPLETED
    val stripeColor = if (isEvenRow) TableRowMidnightGreen else TableRowLowKeyFuchsia
    val cellStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, color = Color.White)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(stripeColor)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.title,
            style = cellStyle,
            textDecoration = if (completed) TextDecoration.LineThrough else null,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(2f),
        )
        Text(
            text = formatTimestampCompact(item.createdAt),
            style = cellStyle.copy(fontFamily = FontFamily.Monospace, textAlign = TextAlign.Center),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1.6f),
        )
        Text(
            text = priorityLabel(item.priority),
            style = cellStyle.copy(textAlign = TextAlign.Center),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1.1f),
        )
        Row(modifier = Modifier.weight(1.3f), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = completed,
                onCheckedChange = onToggleCompleted,
                // Ambient theme colors (e.g. a dark outline in light mode) would be low-contrast
                // against this row's always-dark background, so these are fixed regardless of theme.
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.White,
                    uncheckedColor = Color.White,
                    checkmarkColor = TableRowMidnightGreen,
                ),
            )
            IconButton(onClick = onArchive) {
                Icon(Icons.Filled.Archive, contentDescription = "Archive", tint = Color.White)
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
