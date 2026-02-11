package com.example.deviceusagetracker.presentation.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.deviceusagetracker.domain.model.AppUsage
import com.example.deviceusagetracker.presentation.viewModel.UsageViewModel

@Composable
fun DashboardScreen(
    viewModel: UsageViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        // ✅ Loading State
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // ✅ Error State
        uiState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        // ✅ Success State
        else -> {

            if (uiState.usageList.isEmpty()) {
                EmptyUsageScreen()
            } else {
                UsageList(uiState.usageList)
            }

        }
    }
}

@Composable
fun EmptyUsageScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No usage data found",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun UsageList(
    list: List<AppUsage>
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {

        items(list) { usage ->

            UsageItem(usage)

        }
    }
}

@Composable
fun UsageItem(
    usage: AppUsage
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text = usage.appName,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Category: ${usage.category}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Usage: ${usage.usageMinutes} minutes",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
