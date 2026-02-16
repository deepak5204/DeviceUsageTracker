package com.example.deviceusagetracker.presentation.permission

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deviceusagetracker.utils.PermissionUtils
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay


@Composable
fun PermissionScreen(
    onPermissionGranted: () -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isChecking by remember { mutableStateOf(true) }
    var isGranted by remember { mutableStateOf(false) }


    fun checkPermission() {
        isChecking = true
        val granted = PermissionUtils.isUsagePermissionGranted(context)
        isGranted = granted
        isChecking = false
    }


    // Initial check
    LaunchedEffect(Unit) {
        delay(300)
        checkPermission()
    }

    // Lifecycle Resume Check (MOST IMPORTANT)
    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                checkPermission()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {

            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Dynamic Title
                Text(
                    text = when {
                        isChecking -> "Checking Permission..."
                        isGranted -> "Permission Granted"
                        else -> "Usage Access Required"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        isGranted -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurface
                    }

                )

                Spacer(Modifier.height(12.dp))

                // Dynamic Description
                Text(
                    text = when {
                        isGranted ->
                            "Great! You have granted usage access. You can now track app usage."
                        else ->
                            "Allow usage access to track screen time and help you manage daily limits."
                    },
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(28.dp))

                if (isChecking) {
                    CircularProgressIndicator()
                }

                // Grant Button
                Button(
                    onClick = {
                        PermissionUtils.openUsageAccessSettings(context)
                    },
                    enabled = !isGranted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(
                        "Grant Permission"
                    )
                }
                Spacer(Modifier.height(12.dp))

                // Check Again Button
                OutlinedButton(
                    onClick = {
                        val granted =
                            PermissionUtils.isUsagePermissionGranted(context)

                        isGranted = granted

                        if (granted) {
                            onPermissionGranted()
                        }
                    },
                    enabled =  isGranted,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("I Have Granted Permission")
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PermissionScreenPreview(modifier: Modifier = Modifier) {
    PermissionScreen {  }
}
