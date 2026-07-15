package com.intellisquid.dashboard.todo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellisquid.dashboard.todo.data.TodoItem
import com.intellisquid.dashboard.todo.ui.DashboardViewModel
import com.intellisquid.dashboard.todo.ui.components.ArchivedTodoRow

@Composable
fun ArchiveScreen(viewModel: DashboardViewModel, contentPadding: PaddingValues) {
    val items by viewModel.archivedItems.collectAsState()
    var pendingDelete by remember { mutableStateOf<TodoItem?>(null) }

    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize().padding(contentPadding), contentAlignment = Alignment.Center) {
            Text(
                "Archived to-dos stay here forever until you delete them yourself.",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(items, key = { it.id }) { todo ->
                ArchivedTodoRow(
                    item = todo,
                    onRestore = { viewModel.restoreFromArchive(todo) },
                    onDeleteForever = { pendingDelete = todo },
                )
            }
        }
    }

    pendingDelete?.let { todo ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("Delete forever?") },
            text = { Text("\"${todo.title}\" will be permanently deleted. This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.purge(todo)
                    pendingDelete = null
                }) { Text("Delete forever") }
            },
            dismissButton = {
                TextButton(onClick = { pendingDelete = null }) { Text("Cancel") }
            },
        )
    }
}
