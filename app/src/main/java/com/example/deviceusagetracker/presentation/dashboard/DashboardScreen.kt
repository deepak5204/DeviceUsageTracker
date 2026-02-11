package com.example.deviceusagetracker.presentation.dashboard


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.deviceusagetracker.presentation.viewModel.UsageViewModel

@Composable
fun DashboardScreen(
    viewModel: UsageViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        uiState.error != null -> {
            Text(text = uiState.error ?: "Unknown error")
        }

        else -> {
            UsageList(uiState.usageList)
        }
    }
}


@Composable
fun UsageList(list: List<com.example.deviceusagetracker.domain.model.AppUsage>) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {

        items(list) { usage ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {

                Column(modifier = Modifier.padding(12.dp)) {

                    Text(text = usage.appName, style = MaterialTheme.typography.titleMedium)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "Category: ${usage.category}")

                    Text(text = "Usage: ${usage.usageMinutes} minutes")
                }
            }
        }
    }
}
