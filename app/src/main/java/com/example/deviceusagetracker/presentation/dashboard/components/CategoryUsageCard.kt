package com.example.deviceusagetracker.presentation.dashboard.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.deviceusagetracker.domain.model.AppUsage
import com.example.deviceusagetracker.domain.model.CategoryUsage
import com.example.deviceusagetracker.domain.model.UsageStatus
import com.example.deviceusagetracker.presentation.utils.toBitmap

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
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(start=16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
        ) {

            // Top Row (Icon + Name + Badge)
            Row(verticalAlignment = Alignment.CenterVertically) {

                usage.icon?.let {
                    Image(
                        bitmap = it.toBitmap().asImageBitmap(),
                        contentDescription = usage.appName,
                        modifier = Modifier.size(42.dp)
                            .weight(1.5f).padding(top = 0.dp)
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Used: ${usage.usageMinutes} min")
                Text("Remaining: ${usage.remainingMinutes} min")
            }

//            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = usage.progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                strokeCap = StrokeCap.Round
            )

//            Spacer(modifier = Modifier.height(4.dp))

            // Buttons
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                TextButton(onClick = {}, contentPadding = PaddingValues(0.dp)) {
                    Text(text = "Manage Limit")
                }

                TextButton(onClick = onViewDetails,  contentPadding = PaddingValues(0.dp)) {
                    Text("View Details")
                }
            }
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
        shape = RoundedCornerShape(50),
        color = bgColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            color = textColor,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

