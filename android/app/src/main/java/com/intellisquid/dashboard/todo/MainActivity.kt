package com.intellisquid.dashboard.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.intellisquid.dashboard.todo.navigation.DashboardApp
import com.intellisquid.dashboard.todo.ui.DashboardViewModel
import com.intellisquid.dashboard.todo.ui.DashboardViewModelFactory
import com.intellisquid.dashboard.todo.ui.theme.DashboardTodoTheme

class MainActivity : ComponentActivity() {

    private val viewModel: DashboardViewModel by viewModels {
        val app = application as DashboardApplication
        DashboardViewModelFactory(app.repository, app.backupManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DashboardTodoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DashboardApp(viewModel)
                }
            }
        }
    }
}
