package com.keerthi.vidyarthibus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.keerthi.vidyarthibus.data.model.Bus
import com.keerthi.vidyarthibus.ui.theme.PremiumOrange
import com.keerthi.vidyarthibus.ui.theme.SuccessGreen
import com.keerthi.vidyarthibus.ui.theme.WarningYellow
import com.keerthi.vidyarthibus.ui.theme.ErrorRed

@Composable
fun BusCard(bus: Bus, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .graphicsLayer {
                shadowElevation = 12f
                shape = RoundedCornerShape(20.dp)
                clip = true
            }
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (bus.isPremium) Color(0xFFFFFDF0) else MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // Bus Image Header
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)) {
                AsyncImage(
                    model = bus.busImageUrl,
                    contentDescription = "Bus Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Premium Badge
                if (bus.isPremium) {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .align(Alignment.TopEnd)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.horizontalGradient(listOf(Color(0xFFFFD700), PremiumOrange)))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("PREMIUM", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
                
                // Overlay Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))))
                )
                
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(bus.busNumber, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Text(bus.routeName, color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${bus.city}, ${bus.state}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    
                    Badge(
                        containerColor = when(bus.crowdLevel) {
                            "EMPTY" -> SuccessGreen
                            "SEATS_AVAILABLE" -> WarningYellow
                            else -> ErrorRed
                        },
                        contentColor = Color.White
                    ) {
                        Text(
                            text = when(bus.crowdLevel) {
                                "EMPTY" -> "Empty"
                                "SEATS_AVAILABLE" -> "Available"
                                else -> "Full"
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Next: ${bus.nextStop}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(bus.arrivalTime, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                CrowdMeter(percentage = bus.crowdPercentage)
            }
        }
    }
}
