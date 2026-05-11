package com.keerthi.vidyarthibus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.keerthi.vidyarthibus.ui.components.AppToolbar
import com.keerthi.vidyarthibus.utils.Resource
import com.keerthi.vidyarthibus.viewmodel.BusViewModel

@Composable
fun LiveTrackingScreen(navController: NavController, routeId: String, viewModel: BusViewModel = hiltViewModel()) {
    val busState by viewModel.selectedBus.collectAsState()
    
    androidx.compose.runtime.LaunchedEffect(routeId) {
        viewModel.getBusById(routeId)
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Live Tracking",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            // Mock Map View
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Text("Real-time Map View", color = Color.Gray)
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Red, modifier = Modifier.size(40.dp))
            }
            
            // Bus Info Sheet
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 16.dp
            ) {
                when(val state = busState) {
                    is Resource.Success -> {
                        val bus = state.data
                        Column(modifier = Modifier.padding(24.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("${bus?.busNumber}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Spacer(modifier = Modifier.weight(1f))
                                Badge(containerColor = Color(0xFF4CAF50)) { Text("ON TIME", color = Color.White) }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text("Driver: ${bus?.driverName}", style = MaterialTheme.typography.bodyMedium)
                            Text("Current Speed: ${bus?.speed} km/h", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Column {
                                    Text("Next Stop", color = Color.Gray, fontSize = 12.sp)
                                    Text("${bus?.nextStop}", fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Est. Arrival", color = Color.Gray, fontSize = 12.sp)
                                    Text("${bus?.arrivalTime}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                    is Resource.Loading -> {
                        Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
