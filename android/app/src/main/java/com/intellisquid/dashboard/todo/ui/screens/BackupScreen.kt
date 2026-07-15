package com.intellisquid.dashboard.todo.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellisquid.dashboard.todo.backup.BackupManager
import com.intellisquid.dashboard.todo.ui.DashboardViewModel

@Composable
fun BackupScreen(viewModel: DashboardViewModel, contentPadding: PaddingValues) {
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(BackupManager.MIME_TYPE),
    ) { uri -> uri?.let(viewModel::exportBackup) }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri -> uri?.let(viewModel::importBackup) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Backup & restore", style = MaterialTheme.typography.titleLarge)
        Text(
            "Export every active, completed, and archived to-do to a single JSON file you " +
                "choose the location for — device storage, an SD card, or a synced cloud folder. " +
                "Nothing is ever deleted by a restore: imported items are added alongside what's already there.",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            "Android's own automatic backup is also enabled for this app, so its data travels " +
                "with your Google account backup independently of these manual exports.",
            style = MaterialTheme.typography.bodySmall,
        )

        Button(
            onClick = { exportLauncher.launch(BackupManager.suggestedFileName()) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Filled.CloudUpload, contentDescription = null)
            Text(" Export backup")
        }

        Button(
            onClick = { importLauncher.launch(arrayOf(BackupManager.MIME_TYPE)) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Filled.CloudDownload, contentDescription = null)
            Text(" Restore from backup")
        }
    }
}
