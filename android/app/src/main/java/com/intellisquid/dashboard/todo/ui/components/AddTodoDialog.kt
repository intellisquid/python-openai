package com.intellisquid.dashboard.todo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellisquid.dashboard.todo.data.Priority

@Composable
fun AddTodoDialog(onDismiss: () -> Unit, onConfirm: (title: String, notes: String, priority: Priority) -> Unit) {
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.NORMAL) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New to-do") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                )
                TextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                )
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    Priority.values().forEach { option ->
                        Row(
                            modifier = Modifier.selectable(
                                selected = priority == option,
                                onClick = { priority = option },
                            ),
                        ) {
                            RadioButton(selected = priority == option, onClick = { priority = option })
                            Text(option.name.lowercase().replaceFirstChar { it.uppercase() })
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(title, notes, priority) },
                enabled = title.isNotBlank(),
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
