package com.example.deviceusagetracker.presentation.dashboard

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.deviceusagetracker.domain.model.AppUsage
import com.example.deviceusagetracker.presentation.viewModel.UsageViewModel
import com.example.deviceusagetracker.presentation.utils.toBitmap

@Composable
fun DashboardScreen(
    viewModel: UsageViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        // Loading State
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Error State
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

        // Success State
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
    Log.d("TAG", "UsageItem: $usage")
    if (usage.usageMinutes > 0) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row {
                    usage.icon?.let {
                        Image(
                            bitmap = it.toBitmap().asImageBitmap(),
                            contentDescription = usage.appName,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(top = 0.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(usage.appName, style = MaterialTheme.typography.titleMedium)
                        Text("Category: ${usage.category}")
                        Text("Usage: ${usage.usageMinutes} min")
                    }
                }
            }
        }
    }
}
