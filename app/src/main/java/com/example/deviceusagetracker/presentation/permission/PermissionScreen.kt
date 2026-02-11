package com.example.deviceusagetracker.presentation.permission

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.deviceusagetracker.utils.PermissionUtils

@Composable
fun PermissionScreen(
    onPermissionGranted: () -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Usage Access Required",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Device Usage Tracker needs Usage Access permission to track app usage time."
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                PermissionUtils.openUsageAccessSettings(context)
            }
        ) {
            Text("Grant Permission")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                if (PermissionUtils.isUsagePermissionGranted(context)) {
                    onPermissionGranted()
                }
            }
        ) {
            Text("I Have Granted Permission")
        }
    }
}
