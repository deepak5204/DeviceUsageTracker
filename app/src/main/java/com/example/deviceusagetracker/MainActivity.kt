package com.example.deviceusagetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deviceusagetracker.data.manager.UsageStatsHelper
import com.example.deviceusagetracker.presentation.permission.PermissionScreen
import com.example.deviceusagetracker.ui.theme.DeviceUsageTrackerTheme
import com.example.deviceusagetracker.utils.PermissionUtils
import kotlin.math.log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val helper = UsageStatsHelper(this)
        helper.logUsageStats()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            DeviceUsageTrackerTheme {

                var hasPermission by remember {
                    mutableStateOf(PermissionUtils.isUsagePermissionGranted(this))
                }

                if (hasPermission) {
                    Text("Permission Granted (Dashboard Placeholder)", modifier = Modifier.padding(24.dp))
                } else {
                    PermissionScreen(
                        onPermissionGranted = {
                            hasPermission = true
                        }
                    )
                }

                // Re-check when returning from Settings
                LaunchedEffect(Unit) {
                    hasPermission =
                        PermissionUtils.isUsagePermissionGranted(this@MainActivity)
                }
            }
//            DeviceUsageTrackerTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }


        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DeviceUsageTrackerTheme {
        Greeting("Android")
    }
}