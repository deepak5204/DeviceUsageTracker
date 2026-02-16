package com.example.deviceusagetracker.presentation.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.deviceusagetracker.domain.model.AppCategory
import com.example.deviceusagetracker.domain.model.AppUsage
import com.example.deviceusagetracker.domain.model.UsageStatus
import com.example.deviceusagetracker.presentation.utils.toBitmap


import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun CategoryUsageCard(
    usage: AppUsage,
    onViewDetails: () -> Unit,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
        ) {
            // Top Row (Icon + Name + Badge)
            Row {
                usage.icon?.let {
                    Image(
                        bitmap = it.toBitmap().asImageBitmap(),
                        contentDescription = usage.appName,
                        modifier = Modifier
                            .size(42.dp)
                            .weight(1.5f)
                            .padding(top = 0.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(7.5f)) {
                    Text(usage.appName, style = MaterialTheme.typography.titleMedium)
                    Text("Category: ${usage.category}")
                }
                StatusBadge(usage.status, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Usage + Remaining
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Used: ${usage.usageMinutes} min")
                Text("Remaining: ${usage.remainingMinutes} min")
            }

            GradientUsageProgress(progress = usage.progress)

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Composable
fun StatusBadge(status: UsageStatus, modifier: Modifier) {

    val (bgColor, textColor, text) = when (status) {
        UsageStatus.SAFE -> Triple(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            "Safe"
        )

        UsageStatus.WARNING -> Triple(
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            "Warning"
        )

        UsageStatus.LIMIT_REACHED -> Triple(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            "Limit Reached"
        )
    }

    Surface(
        shape = RoundedCornerShape(25), color = bgColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = textColor,
            style = MaterialTheme.typography.labelMedium
        )
    }
}


fun getUsageBrush(progress: Float): Brush {

    return when {
        progress <= 0.5f -> {
            Brush.horizontalGradient(
                listOf(Color(0xFF4CAF50), Color(0xFF4CAF50)) // Green
            )
        }

        progress <= 0.8f -> {
            Brush.horizontalGradient(
                listOf(
                    Color(0xFF4CAF50),   // Green
                    Color(0xFFFFC107),   // Yellow
                    Color(0xFFFF9800)    // Orange
                )
            )
        }

        else -> {
            Brush.horizontalGradient(
                listOf(
                    Color(0xFFFFC107),   // Yellow
                    Color(0xFFFF9800),   // Orange
                    Color(0xFFF44336)    // Red
                )
            )
        }
    }
}


@Composable
fun GradientUsageProgress(progress: Float) {

    val brush = getUsageBrush(progress.coerceIn(0f, 1f))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.LightGray.copy(alpha = 0.3f))
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .fillMaxHeight()
                .background(brush)
        )
    }
}


val previewAppUsageList =

    AppUsage(
        icon = ColorDrawable(android.graphics.Color.BLUE),
        packageName = "com.instagram.android",
        appName = "Instagram",
        category = AppCategory.SOCIAL_MEDIA,
        usageMinutes = 95,
        limitMinutes = 120
    )


@Preview
@Composable
fun CategoryUsageCardPreview() {
    CategoryUsageCard(
        usage = previewAppUsageList, onViewDetails = {})
}