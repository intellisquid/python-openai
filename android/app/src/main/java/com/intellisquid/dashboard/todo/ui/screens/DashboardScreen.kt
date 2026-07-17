package com.intellisquid.dashboard.todo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellisquid.dashboard.todo.data.TodoItem
import com.intellisquid.dashboard.todo.data.TodoStatus
import com.intellisquid.dashboard.todo.ui.DashboardViewModel
import com.intellisquid.dashboard.todo.ui.components.ActiveTodoTableRow
import com.intellisquid.dashboard.todo.ui.components.AddTodoDialog
import com.intellisquid.dashboard.todo.ui.components.SortDirection
import com.intellisquid.dashboard.todo.ui.components.SortState
import com.intellisquid.dashboard.todo.ui.components.SortStateSaver
import com.intellisquid.dashboard.todo.ui.components.TodoColumn
import com.intellisquid.dashboard.todo.ui.components.TodoTableHeader
import com.intellisquid.dashboard.todo.ui.components.sortTodos
import com.intellisquid.dashboard.todo.ui.components.toggleSort

@Composable
fun DashboardScreen(viewModel: DashboardViewModel, contentPadding: PaddingValues) {
    val items by viewModel.dashboardItems.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    var activeSortState by rememberSaveable(stateSaver = SortStateSaver) {
        mutableStateOf(SortState(TodoColumn.CREATED, SortDirection.DESCENDING))
    }
    var completedSortState by rememberSaveable(stateSaver = SortStateSaver) {
        mutableStateOf(SortState(TodoColumn.CREATED, SortDirection.DESCENDING))
    }

    Scaffold(
        modifier = Modifier.padding(contentPadding),
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Add to-do")
            }
        },
    ) { innerPadding ->
        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Nothing on your plate. Tap + to add a to-do.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            val active = items.filter { it.status == TodoStatus.ACTIVE }
            val completed = items.filter { it.status == TodoStatus.COMPLETED }
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (active.isNotEmpty()) {
                    item { Text("Active", style = MaterialTheme.typography.titleLarge) }
                    item {
                        TodoTableHeader(
                            sortState = activeSortState,
                            onSort = { column -> activeSortState = toggleSort(activeSortState, column) },
                        )
                    }
                    todoTableRows(
                        items = sortTodos(active, activeSortState),
                        viewModel = viewModel,
                    )
                }
                if (completed.isNotEmpty()) {
                    item {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text("Completed", style = MaterialTheme.typography.titleLarge)
                    }
                    item {
                        TodoTableHeader(
                            sortState = completedSortState,
                            onSort = { column -> completedSortState = toggleSort(completedSortState, column) },
                        )
                    }
                    todoTableRows(
                        items = sortTodos(completed, completedSortState),
                        viewModel = viewModel,
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddTodoDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, notes, priority ->
                viewModel.addTodo(title, notes, priority)
                showAddDialog = false
            },
        )
    }
}

private fun LazyListScope.todoTableRows(
    items: List<TodoItem>,
    viewModel: DashboardViewModel,
) {
    itemsIndexed(items, key = { _, todo -> todo.id }) { index, todo ->
        ActiveTodoTableRow(
            item = todo,
            isEvenRow = index % 2 == 0,
            onToggleCompleted = { viewModel.setCompleted(todo, it) },
            onArchive = { viewModel.archive(todo) },
        )
        if (index < items.lastIndex) {
            Divider()
        }
    }
}
