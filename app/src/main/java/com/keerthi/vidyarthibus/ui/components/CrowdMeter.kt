package com.keerthi.vidyarthibus.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.keerthi.vidyarthibus.ui.theme.EmptyColor
import com.keerthi.vidyarthibus.ui.theme.FullColor
import com.keerthi.vidyarthibus.ui.theme.SeatsAvailableColor

@Composable
fun CrowdMeter(percentage: Int) {
    val color = when {
        percentage <= 30 -> EmptyColor
        percentage <= 70 -> SeatsAvailableColor
        else -> FullColor
    }
    
    val status = when {
        percentage <= 30 -> "Empty"
        percentage <= 70 -> "Seats Available"
        else -> "Full"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Crowd Status: $status", style = MaterialTheme.typography.bodyMedium)
            Text(text = "$percentage%", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = percentage / 100f,
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = color,
            trackColor = Color.LightGray
        )
    }
}
